package com.butchjgo.linkservice.web;

import com.butchjgo.linkservice.common.domain.AccountInfo;
import com.butchjgo.linkservice.common.repository.AccountRepository;
import com.butchjgo.linkservice.common.service.AccountService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController("accountController")
@RequestMapping(path = "accounts")
@Validated
public class AccountInfoController implements AccountService {

    @Value("${linkservice.account.token}")
    String token;

    @Resource(name = "accountPool")
    Map<String, LinkedList<AccountInfo>> accountsPool;

    @Resource(name = "accountRepository")
    AccountRepository accountRepository;


    @GetMapping(headers = "token")
    @ResponseStatus(value = HttpStatus.OK)
    public List<AccountInfo> getAllAccount(@NotNull @RequestHeader("token") String requestToken) {
        List<AccountInfo> list = null;
        if (requestToken.equals(token)) list = accountRepository.findAll();
        return list;
    }

    @GetMapping(headers = "token", params = "server")
    @ResponseStatus(value = HttpStatus.OK)
    public AccountInfo doGetAccount(@NotNull @RequestHeader("token") String requestToken,
                                    @NotNull @RequestParam(name = "server") String server) throws NotFoundException {
        AccountInfo info = new AccountInfo();

        if (requestToken.equals(token) && accountsPool.containsKey(server)) {
            LinkedList<AccountInfo> accounts = accountsPool.get(server);
            synchronized (accounts) {
                info = accounts.pollFirst();
                if (info != null) {
                    accounts.addLast(info);
                } else throw new NotFoundException("account not found", new Exception());
            }
        } else {
            throw new NotFoundException("account not found", new Exception());
        }
        return info;
    }

    @PostMapping(headers = "token")
    @ResponseStatus(value = HttpStatus.CREATED)
    void doAddAccount(@NotNull @RequestHeader("token") String requestToken,
                      @Valid @NotNull @RequestBody AccountInfo account) {
        String server = account.getServer();
        accountRepository.save(account);
        if (accountsPool.containsKey(server)) {
            LinkedList<AccountInfo> accounts = accountsPool.get(server);
            if (accounts.stream().anyMatch(a -> a.equals(account))) {
                int index = accounts.indexOf(account);
                accounts.set(index, account);
            } else {
                accounts.addLast(account);
            }
        } else {
            accountsPool.put(server, new LinkedList<>() {{
                addLast(account);
            }});
        }
    }

    @DeleteMapping(headers = "token")
    @ResponseStatus(value = HttpStatus.OK)
    void doDeleteAccount(@NotNull @RequestHeader("token") String requestToken,
                         @Valid @NotNull @RequestBody AccountInfo account) {
        String server = account.getServer();

        accountRepository.delete(account);

        if (accountsPool.containsKey(server)) {
            LinkedList<AccountInfo> accounts = accountsPool.get(server);

            if (accounts.stream().anyMatch(a -> a.equals(account))) {
                synchronized (accounts) {
                    accounts.remove(account);
                }
            }
        }
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @PostConstruct
    void loadData() {
        try {
            List<AccountInfo> infoList = accountRepository.findAll();
            accountsPool.clear();
            infoList.forEach(account -> {
                if (!accountsPool.containsKey(account.getServer())) {
                    accountsPool.put(account.getServer(), new LinkedList<>() {{
                        addLast(account);
                    }});
                } else {
                    LinkedList list = accountsPool.get(account.getServer());
                    list.addLast(account);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public AccountInfo getAccount(String requestToken, String server) throws NotFoundException {
        return doGetAccount(requestToken, server);
    }
}

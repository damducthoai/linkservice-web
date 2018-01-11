package com.butchjgo.linkservice.web;

import com.butchjgo.linkservice.common.domain.AccountInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping(path = "accounts")
@Validated
public class AccountInfoController {

    @Value("${linkservice.account.token}")
    String token;

    @Value("${account.info.file}")
    private String accountFile;
    @Value("${HOME_DIR}")
    private String homeDir;

    @Autowired
    ObjectMapper objectMapper;

    @Resource(name = "accountPool")
    Map<String, LinkedList<AccountInfo>> accountsPool;

    @GetMapping(headers = "token", params = "server")
    @ResponseStatus(value = HttpStatus.OK)
    AccountInfo accountInfo(@NotNull @RequestHeader("token") String requestToken,
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
        saveData();
    }

    @DeleteMapping(headers = "token")
    @ResponseStatus(value = HttpStatus.OK)
    void doDeleteAccount(@NotNull @RequestHeader("token") String requestToken,
                         @Valid @NotNull @RequestBody AccountInfo account) {
        String server = account.getServer();
        if (accountsPool.containsKey(server)) {
            LinkedList<AccountInfo> accounts = accountsPool.get(server);

            if (accounts.stream().anyMatch(a -> a.equals(account))) {
                synchronized (accounts) {
                    accounts.remove(account);
                }
            }
            saveData();
        }
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @PostConstruct
    void loadData() {

        try {
            File file = new File(homeDir.concat(accountFile));
            if (file.exists()) {
                Map<String,List> data = objectMapper.readValue(file, HashMap.class);
                accountsPool.clear();
                data.forEach((k,v)->{
                    final LinkedList<AccountInfo> accounts = new LinkedList<>();
                    System.out.println(v);
                    accountsPool.put(k, accounts);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    void saveData() {
        try {
            File file = new File(homeDir.concat(accountFile));
            objectMapper.writeValue(file, accountsPool);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

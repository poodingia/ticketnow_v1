package com.ticketnow.account.rest;

import com.ticketnow.account.dto.CustomerDTO;
import com.ticketnow.account.dto.CustomerProfileRequestDTO;
import com.ticketnow.account.dto.ProfileWithRoleDTO;
import com.ticketnow.account.rest.response.APIResponse;
import com.ticketnow.account.rest.response.APIResponseBuilder;
import com.ticketnow.account.service.AccountService;
import com.ticketnow.account.utils.AuthenticationUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountResource {
    private final AccountService accountService;

    public AccountResource(AccountService accountService) {
        this.accountService = accountService;
    }

    @DeleteMapping("/{id}")
    public APIResponse<Void> deleteAccount(@PathVariable(name = "id") final String id) {
        accountService.deleteAccount(id);
        APIResponseBuilder<Void> builder = new APIResponseBuilder<>();
        return builder.ok().build();
    }

    @PutMapping("/me")
    public APIResponse<Void> updateAccount(@RequestBody CustomerProfileRequestDTO request) {
        String id = AuthenticationUtils.extractUserId();
        accountService.updateAccount(id,request);
        APIResponseBuilder<Void> builder = new APIResponseBuilder<>();
        return builder.ok().build();
    }

    @GetMapping("/me")
    public APIResponse<CustomerDTO> getAccount() {
        String id = AuthenticationUtils.extractUserId();
        CustomerDTO dto = accountService.getAccount(id);
        APIResponseBuilder<CustomerDTO> builder = new APIResponseBuilder<>();
        return builder.data(dto).ok().build();
    }

    @GetMapping("")
    public APIResponse<ProfileWithRoleDTO> getAccountByEmail(@RequestParam(name = "email") final String email) {
        ProfileWithRoleDTO dto = accountService.getAccountByEmail(email);
        APIResponseBuilder<ProfileWithRoleDTO> builder = new APIResponseBuilder<>();
        return builder.data(dto).ok().build();
    }

    @PutMapping("{email}")
    public APIResponse<Boolean> updateUserRole(@PathVariable String email) {
        Boolean data = accountService.updateUserRole(email, "admin");
        APIResponseBuilder<Boolean> builder = new APIResponseBuilder<>();
        return builder.data(data).ok().build();
    }

    @GetMapping("/all")
    public APIResponse<List<ProfileWithRoleDTO>> getAllAccounts(@RequestParam(name = "page") final Integer page) {
        List<ProfileWithRoleDTO> dto = accountService.getAllAccounts(page - 1, 10);
        APIResponseBuilder<List<ProfileWithRoleDTO>> builder = new APIResponseBuilder<>();
        return builder.data(dto).ok().build();
    }

}

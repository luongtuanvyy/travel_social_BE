package com.app.service.serviceImpl;

import com.app.entity.Account;
import com.app.payload.request.AccountQueryParam;
import com.app.payload.response.APIResponse;
import com.app.payload.response.SuccessAPIResponse;
import com.app.repository.AccountRepository;
import com.app.service.AccountService;
import com.app.speficication.AccountSpecification;
import com.app.utils.PageUtils;
import com.app.utils.RequestParamsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountSpecification accountSpecification;

    @Autowired
    RequestParamsUtils requestParamsUtils;

    @Override
    public APIResponse filterAccount(AccountQueryParam accountQueryParam) {
        Specification<Account> spec = accountSpecification.getAccountSpecification(accountQueryParam);
        Pageable pageable = requestParamsUtils.getPageable(accountQueryParam);
        Page<Account> response = accountRepository.findAll(spec, pageable);
        return new APIResponse(PageUtils.toPageResponse(response));
    }

    @Override
    public APIResponse blockAccount(Integer id) {
        Account  account = accountRepository.findById(id).get();
        account.setIsActivated(false);
        accountRepository.save(account);
        return new SuccessAPIResponse("Block account successful");
    }

    @Override
    public APIResponse removeAccount(Integer id) {
        accountRepository.deleteById(id);
        return new SuccessAPIResponse("Remove account successful");
    }


}

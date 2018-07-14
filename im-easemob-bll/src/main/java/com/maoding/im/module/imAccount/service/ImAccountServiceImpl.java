package com.maoding.im.module.imAccount.service;

import com.maoding.core.base.BaseService;
import com.maoding.core.bean.ApiResult;
import com.maoding.im.easemob.api.ImUserApi;
import com.maoding.im.easemob.comm.ExUser;
import com.maoding.im.module.imAccount.dao.ImAccountDAO;
import com.maoding.im.module.imAccount.dto.ImAccountDTO;
import com.maoding.im.module.imAccount.dto.ImAccountSyncDTO;
import com.maoding.im.module.imAccount.model.ImAccountDO;
import io.swagger.client.model.NewPassword;
import io.swagger.client.model.Nickname;
import io.swagger.client.model.RegisterUsers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CountDownLatch;

@Service("imAccountService")
public class ImAccountServiceImpl extends BaseService implements ImAccountService {
    private static final Logger logger = LoggerFactory.getLogger(ImAccountServiceImpl.class);

    @Autowired
    private ImUserApi imUserApi;

    @Autowired
    private ImAccountDAO imAccountDAO;

    private String passwordStr = "759D11D6CA510C3E8AAEDE52BC968129";

    /** 创建环信账号 **/
    @Override
    public ApiResult create(ImAccountDTO dto) {
        RegisterUsers users = new RegisterUsers();
        ExUser user = new ExUser().username(dto.getAccountId()).password(passwordStr).nickname(dto.getAccountName());
        users.add(user);
        return imUserApi.createNewIMUserSingle(users);
    }

    /** 批量创建环信账号 **/
    @Override
    public ApiResult createBatch(List<ImAccountDTO> list) {
        RegisterUsers users = new RegisterUsers();
        list.forEach(o -> {
            ExUser user = new ExUser().username(o.getAccountId()).password(passwordStr).nickname(o.getAccountName());
            users.add(user);
        });
        return imUserApi.createNewIMUserBatch(users);
    }

    /** 修改环信账号昵称 **/
    @Override
    public ApiResult modifyNickname(ImAccountDTO dto) {
        Nickname nickname = new Nickname().nickname(dto.getAccountName());
        return imUserApi.modifyIMUserNickNameWithAdminToken(dto.getAccountId(), nickname);
    }

    /** 重置环信账号密码 **/
    @Override
    public ApiResult modifyPassword(ImAccountDTO dto) {
        NewPassword password = new NewPassword().newpassword(passwordStr);
        return imUserApi.modifyIMUserPasswordWithAdminToken(dto.getAccountId(), password);
    }

    /** 删除环信账号 **/
    @Override
    public ApiResult delete(String accountId) {
        return imUserApi.deleteIMUserByUserName(accountId);
    }

    @Override
    public ApiResult deleteIMUserBatch() {
        return imUserApi.deleteIMUserBatch(400L,"0");
    }

    @Override
    public void AccountSynIm() {
        List<ImAccountSyncDTO> list = imAccountDAO.selectAllAccountSynIm();
        createIm(list,imAccountDAO);
    }

    private String createIm(List<ImAccountSyncDTO> accountList, ImAccountDAO imAccountDAO) {
        String msg = "";
//        int threadNum = 8;
//        final CountDownLatch cdl = new CountDownLatch(threadNum);
//        long starttime = System.currentTimeMillis();
//        for (int k = 1; k <= threadNum; k++) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        for (int i = 0; i < accountList.size(); i++) {
//                            ImAccountSyncDTO account = accountList.get(i);
//                            if(account.getImAccountId()==null){
//                                ImAccountDTO dto = new ImAccountDTO();
//                                dto.setAccountId(account.getAccountId());
//                                dto.setAccountName(account.getAccountName());
//                                dto.setPassword("759D11D6CA510C3E8AAEDE52BC968129");
//                                ApiResult api = create(dto);
//                                String errorCode = account.getAccountId()+" exists";
//                                if(api.getCode().equals("0") || api.getError().contains(errorCode)){
//                                    ImAccountDO im = new ImAccountDO();
//                                    im.setDeleted(false);
//                                    im.setId(account.getAccountId());
//                                    im.setLastQueueNo(0L);
//                                    im.setAccountStatus(1);
//                                    imAccountDAO.insert(im);
//                                }
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    } finally {
//                        cdl.countDown();
//                    }
//                }
//            }).start();
//        }
//        try {
//            cdl.await();
//            long spendtime = System.currentTimeMillis() - starttime;
//            System.out.println(threadNum + "个线程花费时间:" + spendtime);
//            msg = "更新成功";
//        } catch (InterruptedException e) {
//            msg = "更新失败";
//        }


        for (int i = 0; i < accountList.size(); i++) {
            ImAccountSyncDTO account = accountList.get(i);
            ImAccountDTO dto = new ImAccountDTO();
            dto.setAccountId(account.getAccountId());
            dto.setAccountName(account.getAccountName());
            dto.setPassword(passwordStr);
            if(account.getImAccountId()==null){
                ApiResult api = create(dto);
                String errorCode = "exists";
                if(api.getCode().equals("0") || api.getMsg().contains(errorCode)){
                    ImAccountDO im = new ImAccountDO();
                    im.setDeleted(false);
                    im.setId(account.getAccountId());
                    im.setLastQueueNo(0L);
                    im.setAccountStatus(1);
                    imAccountDAO.insert(im);
                }
            }else {//修改密码
                modifyPassword(dto);
            }
        }
        return msg;
    }
}

package com.maoding.im.module.imAccount;

import com.maoding.core.bean.ApiResult;
import com.maoding.hxIm.dto.ImQueueDTO;
import com.maoding.im.module.imQueue.service.ImQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/account")
public class ImAccountController {

    @Autowired
    private ImQueueService imQueueService;

    @RequestMapping(value = "/handle", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult handle(@RequestBody ImQueueDTO dto) {
        return imQueueService.handleAccountMessage(dto);
    }
}

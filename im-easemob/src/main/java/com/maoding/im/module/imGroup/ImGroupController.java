package com.maoding.im.module.imGroup;


import com.maoding.core.bean.ApiResult;
import com.maoding.im.constDefine.ImOperation;
import com.maoding.hxIm.dto.ImQueueDTO;
import com.maoding.im.module.imQueue.service.ImQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/group")
public class ImGroupController {

    @Autowired
    private ImQueueService imQueueService;

    @RequestMapping(value = "/handle", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult handle(@RequestBody ImQueueDTO dto) {
        if (dto.getOperation().equalsIgnoreCase(ImOperation.GROUP_MEMBER_ADD) || dto.getOperation().equalsIgnoreCase(ImOperation.GROUP_MEMBER_DELETE))
            return imQueueService.handleGroupMemberMessage(dto);
        return imQueueService.handleGroupMessage(dto);
    }
}

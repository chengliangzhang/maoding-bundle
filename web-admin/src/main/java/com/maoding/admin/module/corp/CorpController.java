package com.maoding.admin.module.corp;

import com.maoding.core.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Wuwq on 2017/07/11.
 */
@Controller
@RequestMapping("/corp")
public class CorpController extends BaseController {

    @RequestMapping("/approveList")
    public void approveList() {}

}

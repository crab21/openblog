package mvc.slice.controller;

import mvc.slice.biz.showinfo.ShowInfoService;
import mvc.slice.common.ConstantNumber;
import mvc.slice.pojo.BlogBriefInfo;
import mvc.slice.pojo.paging.PageInfoBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by k on 11/1/17.
 */
@Controller
public class MainPages {

    @ModelAttribute
    public PageInfoBean setPageInfoBean() {
        return new PageInfoBean();
    }

    Logger logger = Logger.getLogger(MainPages.class);

    /**
     * 注入service类 用于信息的展示
     */
    @Inject
    ShowInfoService showInfoService;

    /**
     * @param model
     * @return
     */
    @RequestMapping("/welcome")
    public String index(Model model) {
        List<BlogBriefInfo> briefInfo = showInfoService.findBriefInfo(ConstantNumber.COUNT_NUMBER_MAIN);
        List<BlogBriefInfo> briefInfoOfEvalReadNum =  showInfoService.findBriefInfoByEvalReadNumber(ConstantNumber.COUNT_NUMBER_MAIN);
        model.addAttribute("briefInfo",briefInfo);
        model.addAttribute("briefInfoOfEvalReadNum",briefInfoOfEvalReadNum);
        return "show_welcome";
    }

    /**
     * 首页
     *
     * @param model
     * @return
     */
    @RequestMapping("/showmore")
    public String showMore(Model model) {
        PageInfoBean pageInfoBean = new PageInfoBean();
        pageInfoBean.setShowSize(6);
        List<BlogBriefInfo> blogBriefInfo = showInfoService.findAllInfo(pageInfoBean);

        //todo 判空放在helper中进行
        if (blogBriefInfo != null) {
            PageInfoBean page = showInfoService.findPageInfos(pageInfoBean);
            model.addAttribute("blogBriefInfo", blogBriefInfo);
            model.addAttribute("page", page);
            return "show_more";
        } else {
            return ConstantNumber.ERROR_PAGE;
        }
    }

    /**
     * 分页
     *
     * @param model
     * @param pageInfoBean
     * @return
     */
    @RequestMapping(value = "/showmore", params = "pageFlag")
    public String showmores(Model model, @ModelAttribute PageInfoBean pageInfoBean, @RequestParam("artType") String artType) {
        //todo 分页中动态展示的页数
        pageInfoBean.setShowSize(6);
        List<BlogBriefInfo> blogBriefInfo = StringUtils.isBlank(artType)?showInfoService.findAllInfo(pageInfoBean):showInfoService.findOneTypeInfo(artType,pageInfoBean);
        return new MainPagesHelper().showmores(model, blogBriefInfo, pageInfoBean, showInfoService);
    }

    /**
     *
     * @return
     */
    @RequestMapping("/showinfo")
    public String subInfoJsp() {
        return "show_subinfo";
    }
}



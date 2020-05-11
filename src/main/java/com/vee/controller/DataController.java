/** 
 * Project Name:invoice 
 * File Name:DataController.java 
 * Package Name:com.vee.controller 
 * Date:2020年5月11日下午7:42:43 
 * Copyright (c) 2020,byx9577@qq.com All Rights Reserved. 
 * 
*/  
/** 
 * Project Name:invoice 
 * File Name:DataController.java 
 * Package Name:com.vee.controller 
 * Date:2020年5月11日下午7:42:43 
 * Copyright (c) 2020, chenzhou1025@126.com All Rights Reserved. 
 * 
 */   

package com.vee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/** 
 * ClassName:DataController <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2020年5月11日 下午7:42:43 <br/> 
 * @author   Administrator 
 * @version   
 * @since    JDK 1.6 
 * @see       
 */
/** 
 * ClassName: DataController <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason: TODO ADD REASON(可选). <br/> 
 * date: 2020年5月11日 下午7:42:43 <br/> 
 * 
 * @author Administrator 
 * @version  
 * @since JDK 1.6 
 */
@Controller
public class DataController {
	@RequestMapping(value = "/index", method = RequestMethod.GET)
    public String showBlogs() {
        return "index";
    }
}

package com.szl.zhaozhao2.util;

public class Contants {

	public static final String DATABASE_NAME = "acquaintancehelp.db";

	/** 融云 appkey */
	public static final String rongyun_appkey = "tdrvipksrgmk5";

	/** 新浪微博开放平台appkey */
	public static final String sina_weibo_appkey = "3642152354";

	/** TuSDKappkey */
	public static final String tu_appkey = "e3a44cb77969f40f-00-236nn1";

	/** 获取周边位置 */
	public static final String LocationAt_key = "PP6BZ-JZC3W-CQWRR-OC6UL-VNQST-FSBL7";

	/** 物流信息 */
	public static final String logistics_key = "fa4f51112250a53e";

	/** 支付宝 */
	public static final String PARTNER = "2088711541352256";
	public static final String SELLER = "shurenbang@shurenbang.net";
	public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALsWCepkZbyAlPbqoNsuLzUISSbu4H1+u9GLJ/zc11MjFJqda10GoA+KCpqB3BAHr00f2Lfn+OQcBulcgXHhw8gFh1Mxm+xFC3iFo4KdHUenHrUUSbhKnVgfDtqzYPosXa7vMesgDboRvOgenVTArDLdfCrwTvIjvfDWabEcttGNAgMBAAECgYAC2hAmoYcne3JJ3UO8c5TGoyyySjvjzdXBGPTwFtG1c7hxruqfDWXNNtZ4ing3Slayv0SHcSLLH14/mmhiuwVH9ZOo1SbTDk3j+OMEgr07TOFgQckfAJCBlE2YC8JELK+ip9B0baNOLIH6FGk5rq8SigZNhf9bA0U7POfTbfFaoQJBAO3YzQH9bjYwqEjaPARP0syzp5uK7bYFpW1Tb7j3WmuG/q7nrDolF2mzuKsfE+FS6pQ+oF6+wRWA6brDXCAE7nkCQQDJXXE4rQu8GQNKHTnYrSh1ytt11F8Ii9+WhkiMp6e1nK/hMNBtiVs1c2GT9aUXiB6/aj7lY9tOPWj2eenuZma1AkAdXDfiWQBz3AnKBHaIKbph3oOAJeQ2JfhHyJbwBEi7IUzrFloiS1XajH7tUMbJd8zRQ/HUAEQhfWpczaTfpvwxAkEAutp4y8zYDM1xHf8MxKG538RD7Y0KOpYA/l7RR6PznjUth9uSLXK+LlVJANF7RuDLF3hxsM7+nBWkJsNubgib2QJBAKZmtcQuaDRoXoNtz/KChkYzMW4MjmxnPxc6FCDk3eCKFJfOHtBslLMaTTjc4IQ2TUeqTVOmxi1YrVKL9tYjav0=";
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	/** 线上服务器 */
	// public static final String BASE_REQUEST_URL =
	// "http://182.92.99.129/client_android.action";

	/** 线上服务器 */
	// public static final String PAY_BASE = "http://mapi.shurenbang.net/";
	/** 测试服务器 */
	public static final String PAY_BASE = "http://testapi.shurenbang.net/";

	public static final String BASE_REQUEST_URL = PAY_BASE
			+ "client_android.action";

	/** 上传文件 */
	public static final String UploadFileUrl = PAY_BASE
			+ "servlet/JJUploadFileServlet";

	/** 上传头像 */
	public static final String UploadAvatarUrl = PAY_BASE
			+ "servlet/JJUploadFaceServlet";

	/** 上传图片 */
	public static final String UploadUrl = PAY_BASE
			+ "servlet/JJUploadImageServlet";

	/** 帮助地址 */
	public static final String HelpUrl = PAY_BASE + "shurenbang/help/index.jsp";


	/** 外网地址 ： http://www.jointtax.com/newzz */
	public static final String BASE_URL = "http://121.52.209.241/newzz/restful";

	/** 单位用户注册*/
	public static final String ApplyForUser= "/userManager/applyForUser";

	/** 实习生注册扫描后提交*/
	public static final String QRCodeScan = "/register/qrCodeScan";

    /** 实习生注册时信息确认*/
	public static final String Affirm = "/register/affirm";

	/** 实现生注册*/
	public static final String UserRegister = "/register/userRegister";

	/** 普通用户注册*/
	public static final String UserRegister2 = "/register/userRegister2";

	/** 用户登陆*/
	public static final String Login ="/userManager/chkUser";

	/** 获取手机验证码*/
	public static final String SendCode = "/register/sendCode";

	/** 添加单位信息*/
	public static final String OfficeInfo = "/office/officeInfo";

	/** 职位查询*/
	public static final String JobList = "/job/jobList";

}

package com.example.springbootaop.aop;

import com.example.springbootaop.model.User;
import com.google.gson.Gson;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class LogAspect {

	/**
	 * 日志打印
	 */
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 使用Pointcut给这个方法定义切点，即UserService中全部方法均为切点。<br>
	 * 这里在这个log方法上面定义切点，然后就只需在下面的Before、After等等注解中填写这个切点方法"log()"即可设置好各个通知的切入位置。
	 * 其中：
	 * <ul>
	 *     <li>execution：代表方法被执行时触发</li>
	 *     <li>*：代表任意返回值的方法</li>
	 *     <li>com.example.springbootaop.service.impl.UserServiceImpl：这个类的全限定名</li>
	 *     <li>(..)：表示任意的参数</li>
	 * </ul>
	 */
	@Pointcut("execution(* com.example.springbootaop.api..*.*(..))")
	public void log() {

	}

	/**
	 * 前置通知：在被代理方法之前调用
	 */
	@Before("log() && args(user)")
	public void doBefore(User user) {
		logger.warn("调用方法之前：");
		logger.warn("接收到请求！");
		logger.warn("得到用户id：" + user.getId());
	}

	/**
	 * 后置通知：在被代理方法之后调用
	 */
	@After("log()")
	public void doAfter() {
		logger.warn("调用方法之后：");
		logger.warn("打印请求内容完成！");
	}

	/**
	 * 返回通知：被代理方法正常返回之后调用
	 */
	@AfterReturning("log()")
	public void doReturning() {
		logger.warn("方法正常返回之后：");
		logger.warn("完成返回内容！");
	}

	/**
	 * 异常通知：被代理方法抛出异常时调用
	 */
	@AfterThrowing("log()")
	public void doThrowing() {
		logger.error("方法抛出异常！");
	}

	/**
	 * 环绕通知
	 */
	@Around("log()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes sra = (ServletRequestAttributes) ra;
		HttpServletRequest request = sra.getRequest();

		String url = request.getRequestURL().toString();
		String method = request.getMethod();
		String uri = request.getRequestURI();
		String queryString = request.getQueryString();
		Object[] args = pjp.getArgs();
		String params = "";
		Gson gson = new Gson();
		//获取请求参数集合并进行遍历拼接
		if(args.length>0){
			if("POST".equals(method)){
				Object object = args[0];
				Map map = getKeyAndValue(object);
				params = gson.toJson(map);
			}else if("GET".equals(method)){
				params = queryString;
			}
		}


		logger.info("请求开始===地址:"+url);
		logger.info("请求开始===类型:"+method);
		logger.info("请求开始===参数:"+params);

		// result的值就是被拦截方法的返回值
		Object result = pjp.proceed();

		logger.info("请求结束===返回值:" + gson.toJson(result));
		return result;
	}

	public static Map<String, Object> getKeyAndValue(Object obj) {
		Map<String, Object> map = new HashMap<>();
		// 得到类对象
		Class userCla = (Class) obj.getClass();
		/* 得到类中的所有属性集合 */
		Field[] fs = userCla.getDeclaredFields();
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
			f.setAccessible(true); // 设置些属性是可以访问的
			Object val = new Object();
			try {
				val = f.get(obj);
				// 得到此属性的值
				map.put(f.getName(), val);// 设置键值
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

		}
		return map;
	}


}
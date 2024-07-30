package com.xiyue.admin;

import cn.hutool.json.JSONUtil;
import com.xiyue.admin.config.ApiContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class XiyueAdminApplicationTests {

	@Autowired
	private ApiContext apiContext;

/*	@Autowired
	private SysRoleMapper sysRoleMapper;


	@Autowired
	private SysUserMapper sysUserMapper;*/

	/*@Autowired
	private Map<String, HCL> hclMap;*/

	public class User{
		private String name;
	}

	public class Age{
		private String age;
	}

	@Test
	public void select() {
		String json = "[\n" +
				"  {\n" +
				"\t\t\"name\": \"hcl\"\n" +
				"\t},\n" +
				"\t{\n" +
				"\t\t\"age\": \"18\"\n" +
				"\t}\n" +
				"]";
		User user = (User) JSONUtil.parseArray(json).get(0);
		Age age = (Age) JSONUtil.parseArray(json).get(0);

/*		apiContext.setCurrentSysId(1L);
		sysUserMapper.selectList(null).forEach(e -> System.out.println(e + " HCl"));*/
		/*hclMap.entrySet().forEach(e -> {
			System.out.println(e.getKey());
		});
		HCL hcl = hclMap.get("demoStpUtil");
		hcl.test();*/





	}

}

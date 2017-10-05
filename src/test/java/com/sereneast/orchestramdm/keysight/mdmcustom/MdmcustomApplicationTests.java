package com.sereneast.orchestramdm.keysight.mdmcustom;

import com.orchestranetworks.schema.Path;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraContent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MdmcustomApplicationTests {

	@Test
	public void contextLoads() {
	}
	public static void main(String[] args) throws IllegalAccessException {
		Map<String,Path> fields = new HashMap<>();
		Field[] accountPathFields = Paths._Account.class.getDeclaredFields();
		for(Field pathField: accountPathFields){
			System.out.println(!Modifier.isPrivate(pathField.getModifiers()));
			if(!Modifier.isPrivate(pathField.getModifiers())) {
				Path path = (Path) pathField.get(null);
				String key = path.format().replaceAll("\\.\\/", "");
				if(!key.contains("DaqaMetaData/")) {
					System.out.println(path);
					System.out.println(key);
					fields.put(key,path);
				}
			}
		}
	}
}

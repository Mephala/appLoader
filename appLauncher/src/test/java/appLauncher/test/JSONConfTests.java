package appLauncher.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.UUID;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;

import appLauncher.conf.PersistedConfiguration;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class JSONConfTests {

	@Test
	public void testConfigurationConversionToJSON() throws JsonGenerationException, JsonMappingException, IOException {
		String username = UUID.randomUUID().toString();
		String pw = UUID.randomUUID().toString();
		Boolean saveConf = System.currentTimeMillis() % 2 == 0;
		PersistedConfiguration pc = new PersistedConfiguration();
		pc.setSaveConfiguration(saveConf);
		pc.setPassword(pw);
		pc.setUsername(username);
		ObjectMapper om = new ObjectMapper();
		String serialized = om.writeValueAsString(pc);
		System.out.println(serialized);
		PersistedConfiguration pc2 = om.readValue(serialized, PersistedConfiguration.class);
		assertTrue(pc2.equals(pc));
	}

}

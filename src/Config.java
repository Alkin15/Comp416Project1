import java.util.*;
import java.util.Properties;

public class Config
{
	Properties configFile;
	public Config()
	{
		configFile = new java.util.Properties();
		try {
			configFile.load(this.getClass().getClassLoader().
					getResourceAsStream("config.cfg"));
		}catch(Exception eta){
			eta.printStackTrace();
		}
	}

	/**
	    * This function is used to get the values from the config file.
	    * @param key is used to get the correct value from the config file.
	    * @return returns the value of the config file.
	    */
	public String getProperty(String key)
	{
		String value = this.configFile.getProperty(key);
		return value;
	}
}
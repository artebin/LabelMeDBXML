package net.trevize.labelme;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Configuration {

	public static final String config_properties_filename = "./config.properties";

	public static Properties prop;

	public static String LabelMeImagesPath;
	public static String key_LabelMeImagesPath = "LabelMeImagesPath";

	public static String LabelMeAnnotationsPath;
	public static String key_LabelMeAnnotationsPath = "LabelMeAnnotationsPath";

	public static String DbxmlInstallLibPath;
	public static String key_DbxmlInstallLibPath = "DbxmlInstallLibPath";

	public static String LabelMeLuceneIndex_KeywordAnalyzerPath;
	public static String key_LabelMeLuceneIndex_KeywordAnalyzerPath = "LabelMeLuceneIndex_KeywordAnalyzerPath";

	public static String LabelMeLuceneIndex_WhitespaceAnalyzerPath;
	public static String key_LabelMeLuceneIndex_WhitespaceAnalyzerPath = "LabelMeLuceneIndex_WhitespaceAnalyzerPath";

	public static String LabelMeDbxmlIndexPath;
	public static String key_LabelMeDbxmlIndexPath = "LabelMeDbxmlIndexPath";

	public static void init() {
		prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream(
					config_properties_filename);
			prop.load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		LabelMeImagesPath = prop.getProperty(key_LabelMeImagesPath);

		LabelMeAnnotationsPath = prop.getProperty(key_LabelMeAnnotationsPath);

		DbxmlInstallLibPath = prop.getProperty(key_DbxmlInstallLibPath);

		LabelMeLuceneIndex_KeywordAnalyzerPath = prop
				.getProperty(key_LabelMeLuceneIndex_KeywordAnalyzerPath);

		LabelMeLuceneIndex_WhitespaceAnalyzerPath = prop
				.getProperty(key_LabelMeLuceneIndex_WhitespaceAnalyzerPath);

		LabelMeDbxmlIndexPath = prop.getProperty(key_LabelMeDbxmlIndexPath);
	}

	public static String getLabelMeImagesPath() {
		if (prop == null) {
			init();
		}
		return LabelMeImagesPath;
	}

	public static String getLabelMeAnnotationsPath() {
		if (prop == null) {
			init();
		}
		return LabelMeAnnotationsPath;
	}

	public static String getDbxmlInstallLibPath() {
		if (prop == null) {
			init();
		}
		return DbxmlInstallLibPath;
	}

	public static String getLabelMeLuceneIndex_KeywordAnalyzerPath() {
		if (prop == null) {
			init();
		}
		return LabelMeLuceneIndex_KeywordAnalyzerPath;
	}

	public static String getLabelMeLuceneIndex_WhitespaceAnalyzerPath() {
		if (prop == null) {
			init();
		}
		return LabelMeLuceneIndex_WhitespaceAnalyzerPath;
	}

	public static String getLabelMeDbxmlIndexPath() {
		if (prop == null) {
			init();
		}
		return LabelMeDbxmlIndexPath;
	}

}

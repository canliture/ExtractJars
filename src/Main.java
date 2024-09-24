import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Main {
    /**
     * usage for example: Main project/xxx/path/.idea/libraries ./libs
     * after running Main, we can get all libs under directory ./libs
     */
    public static void main(String[] args) throws IOException
    {
        if (args.length < 2) return;
        String filePath = args[0];
        String dstDir = args[1];
        Files.list(Paths.get(filePath)).forEach(f -> {
            try {
                extract(f.toFile(), dstDir);
            } catch (ParserConfigurationException | IOException | SAXException e) {
                throw new RuntimeException(e);
            }
        });
    }

    static void extract(File file, String dstDir) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(file);
        NodeList classesList = doc.getElementsByTagName("root");
        for (int i = 0 ; i < classesList.getLength() ; i++)
        {
            Node rootNode = classesList.item(i);
            Node urlAttr = rootNode.getAttributes().getNamedItem("url");
            if (urlAttr instanceof Attr)
            {
                Attr addr = (Attr) urlAttr;
                String jar = addr.getValue();
                String[] prefixes = {"jar://", "file://"};
                String[] suffixes = {"!/"};
                // replace prefixes
                for (String prefix : prefixes)
                {
                    if (jar.startsWith(prefix)) {
                        jar = jar.substring(prefix.length());
                    }
                }
                // replace suffixes
                for (String suffix : suffixes)
                {
                    if (jar.endsWith(suffix))
                    {
                        jar = jar.substring(0, jar.length()-suffix.length());
                    }
                }
                // replace environment
                jar = jar.replace("$USER_HOME$", "/Users/liture");
                jar = jar.replace("$MAVEN_REPOSITORY$", "/Users/liture/.m2/repository");
                if (jar.endsWith("-sources.jar") || !jar.endsWith(".jar"))
                {
                    continue;
                }
                File jarFile = new File(jar);
                Path dstPath = Paths.get(dstDir, jarFile.getName());
                if (!Files.exists(dstPath.getParent()))
                {
                    Files.createDirectories(dstPath.getParent());
                }
                Files.copy(jarFile.toPath(), dstPath, StandardCopyOption.REPLACE_EXISTING);

                System.out.println(dstPath.toAbsolutePath());
            }
        }
    }
}
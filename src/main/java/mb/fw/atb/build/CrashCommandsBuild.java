package mb.fw.atb.build;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import mb.fw.atb.strategy.ATBPattern;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

@Slf4j
public class CrashCommandsBuild {

    static ObjectMapper mapper = new ObjectMapper();
    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        String resourcePath = "src/main/resources";
        String[] fileNames = Paths.get(resourcePath + "/crash/commands").toFile().list();
        List<String> writeStrList = Lists.newArrayList();
        for (String file : fileNames) {
            String writeStr = "crash/commands/" + file;
            log.info("command : {}", writeStr);
            writeStrList.add(writeStr);
        }

        FileUtils.writeByteArrayToFile(new File(resourcePath+"/commands.json"), mapper.writeValueAsString(writeStrList).getBytes());

    }

}
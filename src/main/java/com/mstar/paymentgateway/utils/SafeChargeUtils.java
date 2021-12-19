package com.mstar.paymentgateway.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Component
public class SafeChargeUtils {
    public static void logApmTestFlowDMNParametersToConsole(HashMap<String, Object> parameters) {
        try {
            ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(parameters);
            System.out.println(json);
        } catch (JsonProcessingException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void writeApmTestFlowObjectsToFile(Object object, String ApmName, String type) {
        File folder = new File("src/main/resources/apm/" + ApmName);
        boolean folderExist = folder.exists();
        if (!folderExist) {
            folderExist = folder.mkdirs();
        }

        if (folderExist) {
            try (Writer writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(folder + "/" + type + "_" + System.currentTimeMillis() + ".json"),
                            StandardCharsets.UTF_8))) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                String text = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
                writer.write(text);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

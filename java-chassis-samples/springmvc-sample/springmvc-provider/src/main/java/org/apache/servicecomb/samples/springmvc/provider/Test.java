package org.apache.servicecomb.samples.springmvc.provider;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2019/12/5.
 */
public class Test {
  public static void main(String[] args) throws Exception {
    for (int i = 0; i < 96; i++) {
      File dir = new File("s" + i);
      dir.mkdir();

      File fileEndpoint = new File(dir, "SpringmvcBasicEndpoint" + i + ".java");
      String e = endpoint.replace("org.apache.servicecomb.samples.springmvc.provider",
          "org.apache.servicecomb.samples.springmvc.provider." + "s" + i)
          .replace("@RestSchema(schemaId = \"SpringmvcBasicEndpoint\")",
              "@RestSchema(schemaId = \"SpringmvcBasicEndpoint" + i+ "\")")
          .replace("public class SpringmvcBasicEndpoint",
              "public class SpringmvcBasicEndpoint" + i )
           .replace("@RequestMapping(path = \"/springmvc/basic\"",
               "@RequestMapping(path = \"/springmvc/basic" + i + "\"");
      writeFile(fileEndpoint, e);
      File fileRequest = new File(dir, "SpringmvcBasicRequestModel.java");
      writeFile(fileRequest, request.replace("org.apache.servicecomb.samples.springmvc.provider",
          "org.apache.servicecomb.samples.springmvc.provider." + "s" + i));
      File fileResponse = new File(dir, "SpringmvcBasicResponseModel.java");
      writeFile(fileResponse, response.replace("org.apache.servicecomb.samples.springmvc.provider",
          "org.apache.servicecomb.samples.springmvc.provider." + "s" + i));
    }
  }

  public static void writeFile(File f, String content) throws Exception {
    FileOutputStream fileOutputStream = new FileOutputStream(f);
    fileOutputStream.write(content.getBytes());
    fileOutputStream.close();
  }

  private static final String endpoint = "package org.apache.servicecomb.samples.springmvc.provider;\n"
      + "\n"
      + "import javax.ws.rs.core.MediaType;\n"
      + "\n"
      + "import org.apache.servicecomb.provider.rest.common.RestSchema;\n"
      + "import org.apache.servicecomb.samples.common.schema.models.Person;\n"
      + "import org.springframework.web.bind.annotation.RequestBody;\n"
      + "import org.springframework.web.bind.annotation.RequestMapping;\n"
      + "import org.springframework.web.bind.annotation.RequestMethod;\n"
      + "\n"
      + "@RestSchema(schemaId = \"SpringmvcBasicEndpoint\")\n"
      + "@RequestMapping(path = \"/springmvc/basic\", produces = MediaType.APPLICATION_JSON)\n"
      + "public class SpringmvcBasicEndpoint {\n"
      + "  @RequestMapping(path = \"/postObject\", method = RequestMethod.POST)\n"
      + "  public SpringmvcBasicResponseModel sayHello(@RequestBody SpringmvcBasicRequestModel requestModel) {\n"
      + "    SpringmvcBasicResponseModel model = new SpringmvcBasicResponseModel();\n"
      + "    model.setResponseId(requestModel.getRequestId());\n"
      + "    model.setResultMessage(requestModel.getName());\n"
      + "    return model;\n"
      + "  }\n"
      + "}";

  private static final String request = "package org.apache.servicecomb.samples.springmvc.provider;\n"
      + "\n"
      + "public class SpringmvcBasicRequestModel {\n"
      + "  private int requestId;\n"
      + "  private String name;\n"
      + "\n"
      + "  public int getRequestId() {\n"
      + "    return requestId;\n"
      + "  }\n"
      + "\n"
      + "  public void setRequestId(int requestId) {\n"
      + "    this.requestId = requestId;\n"
      + "  }\n"
      + "\n"
      + "  public String getName() {\n"
      + "    return name;\n"
      + "  }\n"
      + "\n"
      + "  public void setName(String name) {\n"
      + "    this.name = name;\n"
      + "  }\n"
      + "}";

  private static final String response = "package org.apache.servicecomb.samples.springmvc.provider;\n"
      + "\n"
      + "public class SpringmvcBasicResponseModel {\n"
      + "  private int responseId;\n"
      + "  private String resultMessage;\n"
      + "\n"
      + "  public int getResponseId() {\n"
      + "    return responseId;\n"
      + "  }\n"
      + "\n"
      + "  public void setResponseId(int responseId) {\n"
      + "    this.responseId = responseId;\n"
      + "  }\n"
      + "\n"
      + "  public String getResultMessage() {\n"
      + "    return resultMessage;\n"
      + "  }\n"
      + "\n"
      + "  public void setResultMessage(String resultMessage) {\n"
      + "    this.resultMessage = resultMessage;\n"
      + "  }\n"
      + "}";
}

package com.ly.ta;

import com.ly.ta.tool.JdbcUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;

public class CrudGenerator {

    private String         resourcesDir = "./";

    private VelocityEngine ve;

    private String         restUrl;

    public void generate(String persistenceUnitName, String destDirRelativePath, String restUrl) throws Exception {

        assert (persistenceUnitName != null);
        assert (destDirRelativePath != null);
        assert (restUrl != null);

        //		EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        //		EntityManager em = emf.createEntityManager();

        this.ve = new VelocityEngine();

        this.restUrl = restUrl;

        List<Map<String, Object>> entities = initPersistenceInfo("ta_admin_user");

        File root = new File(destDirRelativePath);
        root.delete();
        root.mkdirs();

        copyResources(root);

        ve.init();

//        generateAppAndSrvJS(ve, entities, root);
//
//        generateControllersAndModulesJS(ve, entities, root);

        generateHTML(ve, entities, root);

        System.out.println("CRUD Web app generated in " + root.getAbsolutePath());

    }

    private void copyResources(File root) throws IOException, URISyntaxException {
        File srcDir = new File(resourcesDir + "static");
        if (srcDir.exists() && srcDir.isDirectory())
            FileUtils.copy(srcDir, root);
        else {

            FileUtils.copyResourcesToDirectory("static", root.getPath());
            ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        }
    }

    private void generateAppAndSrvJS(VelocityEngine ve, List<Map<String, Object>> entities, File root) throws IOException, ResourceNotFoundException,
                                                                                                       ParseErrorException, URISyntaxException {
        File js = new File(root, "js");
        if (!js.exists())
            js.mkdir();

        VelocityContext context = new VelocityContext();
        context.put("entities", entities);
        context.put("restUrl", restUrl);

        Template appTemplate = ve.getTemplate(getResourceFile("js/app_js.vm"));
        Path path = Paths.get(js.getPath(), "app.js");
        System.out.println("File " + path);
        BufferedWriter writer = Files.newBufferedWriter(path);
        appTemplate.merge(context, writer);
        writer.close();

        Template srvTemplate = ve.getTemplate(getResourceFile("js/services_js.vm"));
        path = Paths.get(js.getPath(), "services.js");
        System.out.println("File " + path);
        writer = Files.newBufferedWriter(path);
        srvTemplate.merge(context, writer);
        writer.close();
    }

    private String getResourceFile(String relativePath) throws URISyntaxException {
        File f = new File(resourcesDir + "templates/" + relativePath);
        if (f.exists() && f.isFile())
            return f.getPath();
        else
            return "templates/" + relativePath;
    }

    private void generateHTML(VelocityEngine ve, List<Map<String, Object>> entities, File root) throws IOException, ResourceNotFoundException, ParseErrorException,
                                                                                                URISyntaxException {
        File partials = new File(root, "partials");
        if (!partials.exists())
            partials.mkdir();

        Template formTemplate = ve.getTemplate(getResourceFile("page/ENTITY_form_html.vm"));
        Template listTemplate = ve.getTemplate(getResourceFile("page/ENTITY_list_html.vm"));
        VelocityContext context = new VelocityContext();
        for (Map<String, Object> map : entities) {
            String name = (String) map.get("name");
            File dir = new File(partials, name.toLowerCase());
            if (!dir.exists())
                dir.mkdir();
            context.put("entity", map);
            Path path = Paths.get(dir.getPath(), name.toLowerCase() + "_form.html");
            System.out.println("File " + path);
            BufferedWriter writer = Files.newBufferedWriter(path);
            formTemplate.merge(context, writer);
            writer.close();
            path = Paths.get(dir.getPath(), name.toLowerCase() + "_list.html");
            System.out.println("File " + path);
            writer = Files.newBufferedWriter(path);
            listTemplate.merge(context, writer);
            writer.close();
        }

        Template indexTemplate = ve.getTemplate(getResourceFile("page/index_html.vm"));
        context = new VelocityContext();
        context.put("entities", entities);
        Path path = Paths.get(root.getPath(), "index.html");
        System.out.println("File " + path);
        BufferedWriter writer = Files.newBufferedWriter(path);
        indexTemplate.merge(context, writer);
        writer.close();

    }

    private void generateControllersAndModulesJS(VelocityEngine ve, List<Map<String, Object>> entities, File root) throws IOException, ResourceNotFoundException,
                                                                                                                   ParseErrorException, URISyntaxException {
        File js = new File(root, "js");
        if (!js.exists())
            js.mkdir();

        Template ctlrTemplate = ve.getTemplate(getResourceFile("js/ENTITY_controller_js.vm"));
        Template moduleTemplate = ve.getTemplate(getResourceFile("js/ENTITY_module_js.vm"));
        VelocityContext context = new VelocityContext();
        for (Map<String, Object> map : entities) {
            String name = (String) map.get("name");
            File dir = new File(js, name.toLowerCase());
            if (!dir.exists())
                dir.mkdir();
            context.put("entity", map);
            Path path = Paths.get(dir.getPath(), name.toLowerCase() + "_controller.js");
            System.out.println("File " + path);
            BufferedWriter writer = Files.newBufferedWriter(path);
            ctlrTemplate.merge(context, writer);
            writer.close();
            path = Paths.get(dir.getPath(), name.toLowerCase() + "_module.js");
            System.out.println("File " + path);
            writer = Files.newBufferedWriter(path);
            moduleTemplate.merge(context, writer);
            writer.close();
        }
    }

    private List<Map<String, Object>> initPersistenceInfo(String commandName) throws ClassNotFoundException {
        JdbcUtils jdbcUtils = new JdbcUtils();
        List<FieldInfo> fieldInfoList = null;
        try {
            fieldInfoList = jdbcUtils.test(commandName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        commandName = StringUtils.camelName(commandName);
        Map<String, Object> entityInfo;
        List<Map<String, Object>> entities = new ArrayList<Map<String, Object>>();
        {
            entityInfo = new HashMap<>();
            entities.add(entityInfo);
            String name = commandName;
            entityInfo.put("name", name);
            entityInfo.put("uncapitName", name.substring(0, 1).toLowerCase() + name.substring(1));
            entityInfo.put("pluralName", StringUtils.plural(name));
            boolean hasLink = false;
            boolean hasColl = false;
            Collections.sort(fieldInfoList, (p1, p2) -> p1.getName().compareTo(p2.getName()));
            entityInfo.put("info", fieldInfoList);
            entityInfo.put("hasLinks", hasLink);
            entityInfo.put("hasCollections", hasColl);
        }
        Collections.sort(entities, (p1, p2) -> ((String) p1.get("name")).compareTo((String) p2.get("name")));
        return entities;
    }

}

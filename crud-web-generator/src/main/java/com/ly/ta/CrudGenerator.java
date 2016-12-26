package com.ly.ta;

import com.ly.ta.annotation.TaCrud;
import com.ly.ta.annotation.TaCrudInfo;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class CrudGenerator {

    private String         resourcesDir = "./";
    //    private String         resourcesDir = "./crud-web-generator/src/main/resources/";

    private VelocityEngine ve;

    private String         restUrl;

    public void generate(String className, String destDirRelativePath) throws Exception {

        //		EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        //		EntityManager em = emf.createEntityManager();

        this.ve = new VelocityEngine();
        Class clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        List<Map<String, Object>> entities = null;
        try {
            entities = initPersistenceInfo(clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        File root = new File(destDirRelativePath);
        if (!root.exists()) {
            //            root.delete();
            root.mkdirs();
        }

        //        copyResources(root);
        Properties prop = new Properties();
        prop.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
        prop.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        prop.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init(prop);

        generateJSP(ve, entities, root);

        System.out.println("CRUD Web app generated in " + root.getAbsolutePath());

    }

    private String getResourceFile(String relativePath) throws URISyntaxException {
        //        java.net.URL fileUrl = this.getClass().getResource("/templates/" + relativePath);
        //        File f = null;
        //        try {
        //            f = new File(fileUrl.toURI());
        //        } catch (Exception e) {
        //            f = new File(fileUrl.getPath());
        //        }
        //        if (f.exists() && f.isFile())
        //            return f.getPath();
        //        else
        return "templates/" + relativePath;
    }

    private void generateJSP(VelocityEngine ve, List<Map<String, Object>> entities, File root) throws IOException, ResourceNotFoundException, ParseErrorException,
                                                                                               URISyntaxException {
        File partials = new File(root, "");

        if (!partials.exists())
            partials.mkdir();

        Template editFormTemplate = ve.getTemplate(getResourceFile("page/TADO_edit_jsp.vm"));
        Template listTemplate = ve.getTemplate(getResourceFile("page/TADO_show_jsp.vm"));
        VelocityContext context = new VelocityContext();
        for (Map<String, Object> map : entities) {
            String name = (String) map.get("name");
            File dir = new File(partials, "");
            if (!dir.exists())
                dir.mkdir();
            context.put("entity", map);
            context.put("pageTitle", map.get("pageTitle"));
            context.put("commandName", map.get("commandName"));
            Path path = Paths.get(dir.getPath(), name.toLowerCase() + "_form.jsp");
            if (org.apache.commons.lang.StringUtils.isNotEmpty((String) map.get("viewName"))) {
                path = Paths.get(dir.getPath(), "edit" + map.get("viewName") + ".jsp");
            }
            //
            System.out.println("File " + path);
            BufferedWriter writer = Files.newBufferedWriter(path);
            editFormTemplate.merge(context, writer);
            writer.close();
            //
            path = Paths.get(dir.getPath(), name.toLowerCase() + "_list.jsp");
            if (org.apache.commons.lang.StringUtils.isNotEmpty((String) map.get("viewName"))) {
                path = Paths.get(dir.getPath(), "show" + map.get("viewName") + ".jsp");
            }
            System.out.println("File " + path);
            writer = Files.newBufferedWriter(path);
            listTemplate.merge(context, writer);
            writer.close();
        }

        //        Template indexTemplate = ve.getTemplate(getResourceFile("page/index_html.vm"));
        //        context = new VelocityContext();
        //        context.put("entities", entities);
        //        Path path = Paths.get(root.getPath(), "index.html");
        //        System.out.println("File " + path);
        //        BufferedWriter writer = Files.newBufferedWriter(path);
        //        indexTemplate.merge(context, writer);
        //        writer.close();

    }

    /**
     *
     * @param clazz
     * @return
     * @throws ClassNotFoundException
     */
    private List<Map<String, Object>> initPersistenceInfo(Class clazz) throws ClassNotFoundException {
        Map<String, Object> entityInfo;
        List<Map<String, Object>> entities = new ArrayList<>();
        //        Class clazz = target.getClass();
        if (ReflectionUtils.isEntityExposed(clazz) && clazz.isAnnotationPresent(TaCrud.class)) {
            TaCrud page = (TaCrud) clazz.getAnnotation(TaCrud.class);
            entityInfo = new HashMap<>();
            entities.add(entityInfo);
            String name = clazz.getSimpleName();
            entityInfo.put("name", name);
            entityInfo.put("pageTitle", page.pageTitle());
            entityInfo.put("commandName", page.commandName());
            entityInfo.put("viewName", StringUtils.camelName(page.viewName(), false));
            entityInfo.put("uncapitName", name.substring(0, 1).toLowerCase() + name.substring(1));
            entityInfo.put("pluralName", StringUtils.plural(name));
            List<Field> fields = ReflectionUtils.getAllFields(clazz);
            List<FieldInfo> fieldInfoList = new ArrayList<>();
            boolean hasLink = false;
            boolean hasColl = false;
            for (Field f : fields) {
                FieldInfo fi = null;
                if (f.isAnnotationPresent(TaCrudInfo.class)) {
                    fi = new FieldInfo(f.getName(), f.getType().getSimpleName());
                    TaCrudInfo ano = f.getAnnotation(TaCrudInfo.class);
                    fi.setOrder(ano.order());
                    fi.setLabel(ano.label());
                    fi.setIdTag(ano.idTag());
                    fi.setRequired(ano.required());
                    fi.setDescription(ano.desc());
                    fi.setInputType(ano.inputType());
                }
                if (fi != null) {
                    fieldInfoList.add(fi);

                }
            }
            Collections.sort(fieldInfoList, (p1, p2) -> p1.getOrder() - p2.getOrder());
            entityInfo.put("info", fieldInfoList);
            entityInfo.put("hasLinks", hasLink);
            entityInfo.put("hasCollections", hasColl);
        }
        //
        Collections.sort(entities, (p1, p2) -> ((String) p1.get("name")).compareTo((String) p2.get("name")));
        return entities;
    }

    public void setResourcesDir(String resourcesDir) {
        this.resourcesDir = resourcesDir;
    }
}

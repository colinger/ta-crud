package com.ly.ta.tools.crud;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.ly.ta.CrudGenerator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Goal which generates an Angular JS Administration application from the JPA entities of the model
 *
 */
@Mojo(name = "crudweb", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class CrudWebGeneratorMojo extends AbstractMojo {

    /**
     * Name of the persistent unit defined in the persistence.xml
     */
    @Parameter(property = "doClassName", required = true)
    private String doClassName;

    @Parameter(property = "templateDirectory", required = false)
    private String templateDirectory;

    /**
     * Location of the generated files
     */
    @Parameter(defaultValue = "${project.build.directory}", property = "outputDir", required = false)
    private String outputDirectory;


    public void execute() throws MojoExecutionException {
        System.out.println(String.format("##############%s---%s", doClassName, outputDirectory));
        try {
            CrudGenerator crud = new CrudGenerator();
            crud.setResourcesDir(templateDirectory);
            crud.generate(doClassName, outputDirectory);
        } catch (Exception e) {
            throw new MojoExecutionException("Exception during generation", e);
        }

    }
}

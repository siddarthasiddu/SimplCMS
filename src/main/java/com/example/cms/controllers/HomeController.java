package com.example.cms.controllers;

import com.example.cms.models.Page;
import com.example.cms.services.PageService;
import liqp.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;

@Controller
public class HomeController {

    @Autowired
    private PageService pageService;

    @GetMapping("/**")
    public ResponseEntity<String> genericLoader(final HttpServletRequest request) {
        System.out.println(request.getRequestURI());
        String route = request.getRequestURI();
        System.out.println(route);
        Page page = pageService.getPage(route);
        if (page == null) {
            page = pageService.getPage("/error");
        }
        System.out.println("____________________________________________________________");
        System.out.println(page);
        System.out.println("____________________________________________________________");
        Template template = Template.parse(page.getContent());
        String rendered = template.render("name", "tobi");
        System.out.println(rendered);
        return new ResponseEntity<String>(rendered, HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/resources/**", produces = {"text/json;charset=utf-8", "text/css;charset=utf-8"})
    public ResponseEntity<byte[]> getFile(final HttpServletRequest request) throws IOException {
        String[] URI = request.getRequestURI().split("/resources/");

        final File file = ResourceUtils.getFile("classpath:static/" + URI[1]);
        final InputStream targetStream = new DataInputStream(new FileInputStream(file));

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + file.getName())
                .body(Files.readAllBytes(file.toPath()));
    }

    @GetMapping("/hi")
    public ResponseEntity<String> hii() {
        ResponseEntity<String> responseEntity = new ResponseEntity<String>("<p> YAY </p>", HttpStatus.ACCEPTED);
        return responseEntity;
    }


}
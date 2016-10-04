package com.project.launcher;


import static eu.hurion.vaadin.heroku.VaadinForHeroku.forApplication;
import static eu.hurion.vaadin.heroku.VaadinForHeroku.herokuServer;

import com.project.ui.MyUI;

public class Launcher {

    public static void main(final String[] args) {
            herokuServer(forApplication(MyUI.class)).start();
    }
}
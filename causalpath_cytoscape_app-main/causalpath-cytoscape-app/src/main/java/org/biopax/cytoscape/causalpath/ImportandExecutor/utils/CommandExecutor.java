package org.biopax.cytoscape.causalpath.ImportandExecutor.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import org.biopax.cytoscape.causalpath.CyActivator;
import org.biopax.cytoscape.causalpath.Panel.LegendPanel;
import org.cytoscape.command.CommandExecutorTaskFactory;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.work.*;
import org.cytoscape.work.swing.DialogTaskManager;

public class CommandExecutor {

    public static void execute(String command, CyServiceRegistrar cyServiceRegistrar, LegendPanel legendPanel) {
        CommandExecutorTaskFactory executor = cyServiceRegistrar.getService(CommandExecutorTaskFactory.class);

        List<String> commands = new ArrayList<String>();
        commands.add(command);

        TaskIterator task = executor.createTaskIterator(commands, null);
        SynchronousTaskManager manager = cyServiceRegistrar.getService(SynchronousTaskManager.class);

        legendPanel.getStatusBar().setValue(100);
        legendPanel.getStatusLabel().setText("Success!");
        manager.execute(task);

        legendPanel.getSubmitbutton().setEnabled(false);
        legendPanel.getFormatfileuploadbutton().setEnabled(false);
        try{
            Thread.sleep(3000);

        } catch (InterruptedException e) {
            System.out.println("Sleep for User's Understanding");
            e.printStackTrace();
        }

        legendPanel.getStatusBar().setValue(0);


    }
    public static void execute(String command, CyServiceRegistrar cyServiceRegistrar) {
        CommandExecutorTaskFactory executor = cyServiceRegistrar.getService(CommandExecutorTaskFactory.class);

        List<String> commands = new ArrayList<String>();
        commands.add(command);

        TaskIterator task = executor.createTaskIterator(commands, null);
        SynchronousTaskManager manager = cyServiceRegistrar.getService(SynchronousTaskManager.class);

        manager.execute(task);

    }

    public static void execute(TaskIterator commandTask, CyServiceRegistrar cyServiceRegistrar) {
        CyActivator cyActivator = cyServiceRegistrar.getService(CyActivator.class);
        cyActivator.getSynchronousTaskManager().execute(commandTask);

    }
    public static void execute(TaskIterator commandTask, CyServiceRegistrar cyServiceRegistrar,SynchronousTaskManager synchronousTaskManager) {

        synchronousTaskManager.execute(commandTask);
    }

}

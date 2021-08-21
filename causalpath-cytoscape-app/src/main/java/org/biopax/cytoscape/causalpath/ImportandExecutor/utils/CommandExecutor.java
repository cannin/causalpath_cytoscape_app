package org.biopax.cytoscape.causalpath.ImportandExecutor.utils;

import java.util.ArrayList;
import java.util.List;

import org.biopax.cytoscape.causalpath.Panel.LegendPanel;
import org.cytoscape.command.CommandExecutorTaskFactory;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.work.SynchronousTaskManager;
import org.cytoscape.work.TaskIterator;

public class CommandExecutor {

    public static void execute(String command, CyServiceRegistrar cyServiceRegistrar, LegendPanel legendPanel, int flag) {
        CommandExecutorTaskFactory executor = cyServiceRegistrar.getService(CommandExecutorTaskFactory.class);

        List<String> commands = new ArrayList<String>();
        commands.add(command);

        TaskIterator task = executor.createTaskIterator(commands, null);
        SynchronousTaskManager manager = cyServiceRegistrar.getService(SynchronousTaskManager.class);
        manager.execute(task);
        legendPanel.getStatusBar().setValue(100);
        legendPanel.getStatusLabel().setText("Success!");
        legendPanel.getSubmitbutton().setEnabled(false);
        legendPanel.getFormatfileuploadbutton().setEnabled(false);


    }

    public static void execute(TaskIterator commandTask, CyServiceRegistrar cyServiceRegistrar, int flag) {
        cyServiceRegistrar.getService(SynchronousTaskManager.class).execute(commandTask);
    }
}

package org.cytoscape.sample.internal;

import org.cytoscape.task.read.LoadNetworkFileTaskFactory;
import org.cytoscape.task.visualize.ApplyVisualStyleTaskFactory;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.TaskIterator;

import java.io.File;
import java.io.FileInputStream;

public class importfilethread extends Thread{
    private final LoadNetworkFileTaskFactory loadNetworkFileTaskFactory;

    private CyNetworkView view;
    public File f;
    private ApplyVisualStyleTaskFactory applyVisualStyleTaskFactory;
    public  importfilethread(LoadNetworkFileTaskFactory loadNetworkFileTaskFactory, CyNetworkView view,
                             ApplyVisualStyleTaskFactory applyVisualStyleTaskFactory){
        this.loadNetworkFileTaskFactory = loadNetworkFileTaskFactory;
        this.view = view;
        this.applyVisualStyleTaskFactory = applyVisualStyleTaskFactory;
    }

    @Override
    public void run() {
        super.run();
        System.out.println("file loading ");
        TaskIterator ti= loadNetworkFileTaskFactory.createTaskIterator(f);
        System.out.println("file loaded");
        System.out.println(f);
        try
        {


            FileInputStream fis=new FileInputStream(f);     //opens a connection to an actual file
            System.out.println("file content: ");
            int r=0;
            while((r=fis.read())!=-1)
            {
                System.out.print((char)r);      //prints the content of the file
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        //this.insertTasksAfterCurrentTask(ti);
        System.out.println("After the the current task ");
    }

}

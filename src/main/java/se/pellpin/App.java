package se.pellpin;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import se.pellpin.conf.Job;

import java.io.FileReader;

public class App {

    public Job loadJob(String filename) throws Exception {
        Gson gson = new Gson();
        Job job = gson.fromJson(new JsonReader(new FileReader(filename)), Job.class);
        return job;
    }

    public static void main( String[] args ) {
        try {
            App app = new App();
            Job job = app.loadJob(args[0]);
            job.process();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

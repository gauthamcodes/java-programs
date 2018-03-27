package com.github.mmolimar.kafka.connect.fs.task;

import com.github.mmolimar.kafka.connect.fs.FsSourceTask;
import com.github.mmolimar.kafka.connect.fs.FsSourceTaskConfig;
import com.github.mmolimar.kafka.connect.fs.file.reader.TextFileReader;
import com.github.mmolimar.kafka.connect.fs.policy.SimplePolicy;
import org.apache.kafka.connect.errors.ConnectException;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class FsSourceTaskTest {
    @ClassRule
    public static final TemporaryFolder temporaryFolder = new TemporaryFolder();

    private FsSourceTask task;
    private Map<String, String> taskConfig;

    @Before
    public void setup() throws IOException {
        task = new FsSourceTask();

        taskConfig = new HashMap<String, String>() {{
            put(FsSourceTaskConfig.FS_URIS, String.join(",",
                    temporaryFolder.getRoot().toURI() + File.separator + "dir1",
                    temporaryFolder.getRoot().toURI() + File.separator + "dir2",
                    temporaryFolder.getRoot().toURI() + File.separator + "dir3"));
            put(FsSourceTaskConfig.TOPIC, "topic_test");
            put(FsSourceTaskConfig.POLICY_CLASS, SimplePolicy.class.getName());
            put(FsSourceTaskConfig.FILE_READER_CLASS, TextFileReader.class.getName());
        }};
    }

    @Test(expected = ConnectException.class)
    public void nullProperties() {
        task.start(null);
    }

    @Test(expected = ConnectException.class)
    public void expectedFsUris() {
        Map<String, String> testProps = new HashMap<>(taskConfig);
        testProps.remove(FsSourceTaskConfig.FS_URIS);
        task.start(testProps);
    }

    @Test(expected = ConnectException.class)
    public void expectedPolicyClass() {
        Map<String, String> testProps = new HashMap<>(taskConfig);
        testProps.remove(FsSourceTaskConfig.POLICY_CLASS);
        task.start(testProps);
    }

    @Test(expected = ConnectException.class)
    public void invalidPolicyClass() {
        Map<String, String> testProps = new HashMap<>(taskConfig);
        testProps.put(FsSourceTaskConfig.POLICY_CLASS, Object.class.getName());
        task.start(testProps);
    }

    @Test(expected = ConnectException.class)
    public void expectedReaderClass() {
        Map<String, String> testProps = new HashMap<>(taskConfig);
        testProps.remove(FsSourceTaskConfig.FILE_READER_CLASS);
        task.start(testProps);
    }

    @Test(expected = ConnectException.class)
    public void invalidReaderClass() {
        Map<String, String> testProps = new HashMap<>(taskConfig);
        testProps.put(FsSourceTaskConfig.FILE_READER_CLASS, Object.class.getName());
        task.start(testProps);
    }

    @Test
    public void minimunConfig() {
        task.start(taskConfig);
        task.stop();
    }

    @Test
    public void pollWithoutStart() throws InterruptedException {
        assertNull(task.poll());
        task.stop();
    }

    @Test
    public void checkVersion() {
        assertNotNull(task.version());
        assertFalse("unknown".equalsIgnoreCase(task.version()));
    }

}
package com.github.mmolimar.kafka.connect.fs.policy.hdfs;

import com.github.mmolimar.kafka.connect.fs.FsSourceTaskConfig;
import com.github.mmolimar.kafka.connect.fs.file.reader.TextFileReader;
import com.github.mmolimar.kafka.connect.fs.policy.HdfsFileWatcherPolicy;
import org.apache.hadoop.fs.Path;
import org.apache.kafka.connect.errors.IllegalWorkerStateException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HdfsFileWatcherPolicyTest extends HdfsPolicyTestBase {

    @BeforeClass
    public static void setUp() throws IOException {
        directories = new ArrayList<Path>() {{
            add(new Path(fsUri.toString(), UUID.randomUUID().toString()));
            add(new Path(fsUri.toString(), UUID.randomUUID().toString()));
        }};
        for (Path dir : directories) {
            fs.mkdirs(dir);
        }

        Map<String, String> cfg = new HashMap<String, String>() {{
            String uris[] = directories.stream().map(dir -> dir.toString())
                    .toArray(size -> new String[size]);
            put(FsSourceTaskConfig.FS_URIS, String.join(",", uris));
            put(FsSourceTaskConfig.TOPIC, "topic_test");
            put(FsSourceTaskConfig.POLICY_CLASS, HdfsFileWatcherPolicy.class.getName());
            put(FsSourceTaskConfig.FILE_READER_CLASS, TextFileReader.class.getName());
            put(FsSourceTaskConfig.POLICY_REGEXP, "^[0-9]*\\.txt$");
            put(FsSourceTaskConfig.POLICY_PREFIX_FS + "dfs.data.dir", "test");
            put(FsSourceTaskConfig.POLICY_PREFIX_FS + "fs.default.name", "test");
        }};
        taskConfig = new FsSourceTaskConfig(cfg);
    }

    //This policy does not throw any exception. Just stop watching those nonexistent dirs
    @Test
    @Override
    public void invalidDirectory() throws IOException {
        super.invalidDirectory();
    }

    //This policy never ends at least all watchers die
    @Test
    @Override
    public void hasEnded() throws IOException {
        policy.execute();
        assertFalse(policy.hasEnded());
        policy.interrupt();
        assertTrue(policy.hasEnded());
    }

    //This policy never ends. We have to interrupt it
    @Test(expected = IllegalWorkerStateException.class)
    @Override
    public void execPolicyAlreadyEnded() throws IOException {
        policy.execute();
        assertFalse(policy.hasEnded());
        policy.interrupt();
        assertTrue(policy.hasEnded());
        policy.execute();
    }

}

package com.github.mmolimar.kafka.connect.fs.policy;

import com.github.mmolimar.kafka.connect.fs.FsSourceTaskConfig;
import com.github.mmolimar.kafka.connect.fs.file.FileMetadata;
import com.github.mmolimar.kafka.connect.fs.util.ReflectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.connect.errors.IllegalWorkerStateException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public abstract class PolicyTestBase {

    protected static FileSystem fs;
    protected static Policy policy;
    protected static List<Path> directories;
    protected static FsSourceTaskConfig taskConfig;
    protected static URI fsUri;

    @AfterClass
    public static void tearDown() throws Exception {
        policy.close();
        fs.close();
    }

    @Before
    public void initPolicy() throws Throwable {
        policy = ReflectionUtils.makePolicy((Class<? extends Policy>) taskConfig.getClass(FsSourceTaskConfig.POLICY_CLASS),
                taskConfig);
    }

    @After
    public void cleanDirs() throws IOException {
        for (Path dir : directories) {
            fs.delete(dir, true);
            fs.mkdirs(dir);
        }
        policy.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidArgs() throws Exception {
        taskConfig.getClass(FsSourceTaskConfig.POLICY_CLASS).getConstructor(taskConfig.getClass()).newInstance(null);
    }

    @Test(expected = ConfigException.class)
    public void invalidConfig() throws Throwable {
        ReflectionUtils.makePolicy((Class<? extends Policy>) taskConfig.getClass(FsSourceTaskConfig.POLICY_CLASS),
                new FsSourceTaskConfig(new HashedMap()));
    }

    @Test
    public void interruptPolicy() throws Throwable {
        policy.execute();
        policy.interrupt();
        assertTrue(policy.hasEnded());
    }

    @Test(expected = FileNotFoundException.class)
    public void invalidDirectory() throws IOException {
        for (Path dir : directories) {
            fs.delete(dir, true);
        }
        try {
            policy.execute();
        } finally {
            for (Path dir : directories) {
                fs.mkdirs(dir);
            }
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void listEmptyDirectories() throws IOException {
        Iterator<FileMetadata> it = policy.execute();
        assertFalse(it.hasNext());
        it.next();
    }

    @Test
    public void oneFilePerFs() throws IOException, InterruptedException {
        for (Path dir : directories) {
            fs.createNewFile(new Path(dir, String.valueOf(System.nanoTime() + ".txt")));
            //this file does not match the regexp
            fs.createNewFile(new Path(dir, String.valueOf(System.nanoTime()) + ".invalid"));
        }
        //we wait till FS has registered the files
        Thread.sleep(500);

        Iterator<FileMetadata> it = policy.execute();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertFalse(it.hasNext());
    }

    @Test
    public void recursiveDirectory() throws IOException, InterruptedException {
        for (Path dir : directories) {
            Path tmpDir = new Path(dir, String.valueOf(System.nanoTime()));
            fs.mkdirs(tmpDir);
            fs.createNewFile(new Path(tmpDir, String.valueOf(System.nanoTime() + ".txt")));
            //this file does not match the regexp
            fs.createNewFile(new Path(tmpDir, String.valueOf(System.nanoTime()) + ".invalid"));
        }
        //we wait till FS has registered the files
        Thread.sleep(500);

        Iterator<FileMetadata> it = policy.execute();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertFalse(it.hasNext());
    }

    @Test
    public void hasEnded() throws IOException {
        policy.execute();
        assertTrue(policy.hasEnded());
    }

    @Test(expected = IllegalWorkerStateException.class)
    public void execPolicyAlreadyEnded() throws IOException {
        policy.execute();
        assertTrue(policy.hasEnded());
        policy.execute();
    }

    @Test
    public void dynamicURIs() throws Throwable {
        Path dynamic = new Path(fsUri.toString(), "${G}/${yyyy}/${MM}/${W}");
        fs.create(dynamic);
        Map<String, String> originals = taskConfig.originalsStrings();
        originals.put(FsSourceTaskConfig.FS_URIS, dynamic.toString());
        FsSourceTaskConfig cfg = new FsSourceTaskConfig(originals);
        policy = ReflectionUtils.makePolicy((Class<? extends Policy>) taskConfig.getClass(FsSourceTaskConfig.POLICY_CLASS),
                cfg);
        assertEquals(1, policy.getURIs().size());

        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("G");
        StringBuilder uri = new StringBuilder(dateTime.format(formatter));
        uri.append("/");
        formatter = DateTimeFormatter.ofPattern("yyyy");
        uri.append(dateTime.format(formatter));
        uri.append("/");
        formatter = DateTimeFormatter.ofPattern("MM");
        uri.append(dateTime.format(formatter));
        uri.append("/");
        formatter = DateTimeFormatter.ofPattern("W");
        uri.append(dateTime.format(formatter));
        assertTrue(policy.getURIs().get(0).endsWith(uri.toString()));

    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidDynamicURIs() throws Throwable {
        Path dynamic = new Path(fsUri.toString(), "${yyyy}/${MM}/${mmmmmmm}");
        fs.create(dynamic);
        Map<String, String> originals = taskConfig.originalsStrings();
        originals.put(FsSourceTaskConfig.FS_URIS, dynamic.toString());
        FsSourceTaskConfig cfg = new FsSourceTaskConfig(originals);
        policy = ReflectionUtils.makePolicy((Class<? extends Policy>) taskConfig.getClass(FsSourceTaskConfig.POLICY_CLASS),
                cfg);
    }

}

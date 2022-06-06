package com.shisheng;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * @author shisheng
 */
public class GNote {
    static final Runtime runtime = Runtime.getRuntime();

    static final String target = "../";
    static final String winTarget = "D:\\code\\java\\";
    static final String archTarget = "~\\shisheng\\projects\\";
    static final String repoName = "noted";
    static final String javaCmd = "java";
    static final String gitCmd = "git";
    static final String cloneCmd = """
            git clone https://github.com/shisheng1998/noted.git
            """;

    public static void main(String[] args) throws IOException {

        //check git environment exist
        runtime.exec(gitCmd);

        //check repo exist
        String os = System.getProperty("os.name");
        Path path;
        if (os.toLowerCase().startsWith("win")) {
            path = Paths.get(winTarget + repoName);
        } else {
            path = Paths.get(archTarget + repoName);
        }

        init(path);

        Scanner scan = new Scanner(System.in);
        outMenu();
        while (scan.hasNext()) {
            String keyword = scan.next();

            try {
                if (keyword.equals("1")) {
                    syn(path);
                } else if (keyword.equals("2")) {
                    upload(path);
                } else {
                    System.out.println("input error");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            outMenu();
        }
    }

    private static void outMenu() {
        System.out.println("please input your command:");
        System.out.println("1.syn");
        System.out.println("2.upload");
    }

    private static void init(Path base) throws IOException {
        boolean exists = Files.exists(base);

        //init repo
        if (!exists) {
            Process clone = baseProcess(GitCmdEnums.clone, base.getParent().toFile(), "https://github.com/shisheng1998/noted.git");
            clone.destroy();
        }
    }

    /**
     * 1.检查是fetch是否存在可更新的部分
     * 2.如果有的话进行更新
     *
     * @param base 路径基础
     * @throws IOException IO异常
     */
    private static void syn(Path base) throws IOException {
        baseProcess(GitCmdEnums.fetch, base);
        baseProcess(GitCmdEnums.pull, base);
    }

    private static void upload(Path base) throws IOException {
        baseProcess(GitCmdEnums.add, base, ".");
        baseProcess(GitCmdEnums.commit, base, "-m \"update\"");
        baseProcess(GitCmdEnums.push, base);
    }

    private static Process baseProcess(GitCmdEnums gitCmdEnums, Path base, String... command) throws IOException {
        return baseProcess(gitCmdEnums, base.toFile(), command);
    }

    private static Process baseProcess(GitCmdEnums gitCmdEnums, File base, String... command) throws IOException {
        System.out.println("=================任务" + gitCmdEnums.name() + "开始====================");
        ProcessBuilder processBuilder = new ProcessBuilder(gitCmd, gitCmdEnums.name());
        if (command.length != 0) {
            String[] cmd = new String[command.length + 2];
            cmd[0] = gitCmd;
            cmd[1] = gitCmdEnums.name();
            System.arraycopy(command, 0, cmd, 2, command.length);
            processBuilder.command(cmd);
        }
        processBuilder.directory(base);
        processBuilder.redirectErrorStream(true);
        return processBuilder.start();
    }
}



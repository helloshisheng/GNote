package com.shisheng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author shisheng
 */
public class ProcessUtils {

    public static void outInfo(Process process) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
    }
}

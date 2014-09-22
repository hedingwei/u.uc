package com.ambimmort.u.uc.repository.cli.commands;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 *
 * @author 定巍
 */
public class UcRepositoryAdmin {

    static String baseDir = "./";
    static String dir = "";

    public static void main(String[] args) {
        Options o = new Options();
        o.addOption("C", "create", false, "创建一个策略仓库,该仓库会删除原有的策略仓库");
        o.addOption("b", "basedir", true, "仓库的Base文件夹");
        o.addOption("d", "dir", true, "仓库的Base文件夹");
        o.addOption("t", "messageType", true, "策略类型");
        o.addOption("n", "name", true, "特定策略类型仓库的一个实例名称");
        o.addOption("A", "ADD", false, "向一个策略仓库中添加一个新的策略，若库中已经存在该策略，则更新;反之则添加");
        o.addOption("U", "UPDATE", false, "更新策略仓库中的一个策略，该参数必须和\"-M\"参数一并使用");
        o.addOption("D", "DELETE", false, "删除策略仓库中的一个策略，该参数必须和\"-M\"参数一并使用");
        o.addOption("m", "messageFile", true, "指定策略文件（xml格式）");
        o.addOption("M", "messageNo", true, "指定策略编号（MessageNo）");
        o.addOption("X", "EXPORT", false, "导出一个策略仓库");
        o.addOption("P", "PRINT", false, "打印一个策略");
        o.addOption("V", "VERSION", false, "策略的版本");
        o.addOption("I", "INFO", false, "打印一个策略仓库的信息");

        CommandLineParser parser = new PosixParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(o, args);
        } catch (ParseException ex) {
            usage(o);
            return;
        }
        if (cmd.hasOption("b")) {
            baseDir = cmd.getOptionValue("b");
        }

        if (cmd.hasOption("d")) {
            dir = cmd.getOptionValue("d");
        }

        if (cmd.hasOption("C")) { //Create Repository Branch
            branch_C(o, cmd);
        }

        if (cmd.hasOption("A")) {

        }

    }

    public static void branch_C(Options o, CommandLine cmd) {
        if (cmd.getOptions().length != 4 || cmd.getOptions().length != 3) {
            usage(o);
            return;
        }
        String repoType;
        String instanceName;
        if (cmd.hasOption("t")) {
            repoType = cmd.getOptionValue("t");
        } else {
            usage(o);
            return;
        }
        if (cmd.hasOption("n")) {
            instanceName = cmd.getOptionValue("n");
        } else {
            usage(o);
            return;
        }

        //TODO: logic
    }

    public static void branch_A(Options o, CommandLine cmd) {
        if (cmd.getOptions().length != 4 || cmd.getOptions().length != 5) {
            usage(o);
            return;
        }
        String repoType;
        String instanceName;
        String messageFile;
        if (cmd.hasOption("t")) {
            repoType = cmd.getOptionValue("t");
        } else {
            usage(o);
            return;
        }
        if (cmd.hasOption("n")) {
            instanceName = cmd.getOptionValue("n");
        } else {
            usage(o);
            return;
        }

        if (cmd.hasOption("m")) {
            messageFile = cmd.getOptionValue("m");
        } else {
            usage(o);
            return;
        }

        //TODO: logic
    }

    public static void branch_U(Options o, CommandLine cmd) {
        if (cmd.getOptions().length != 6 || cmd.getOptions().length != 5) {
            usage(o);
            return;
        }
        String repoType;
        String instanceName;
        String messageFile;
        String messageNo;

        if (cmd.hasOption("t")) {
            repoType = cmd.getOptionValue("t");
        } else {
            usage(o);
            return;
        }
        if (cmd.hasOption("n")) {
            instanceName = cmd.getOptionValue("n");
        } else {
            usage(o);
            return;
        }

        if (cmd.hasOption("m")) {
            messageFile = cmd.getOptionValue("m");
        } else {
            usage(o);
            return;
        }

        if (cmd.hasOption("M")) {
            messageNo = cmd.getOptionValue("M");
        } else {
            usage(o);
            return;
        }

        //TODO: logic
    }

    public static void branch_D(Options o, CommandLine cmd) {
        
        if (cmd.getOptions().length != 4 || cmd.getOptions().length != 5) {
            usage(o);
            return;
        }
        String repoType;
        String instanceName;
        String messageNo;

        if (cmd.hasOption("t")) {
            repoType = cmd.getOptionValue("t");
        } else {
            usage(o);
            return;
        }
        if (cmd.hasOption("n")) {
            instanceName = cmd.getOptionValue("n");
        } else {
            usage(o);
            return;
        }

        if (cmd.hasOption("M")) {
            messageNo = cmd.getOptionValue("M");
        } else {
            usage(o);
            return;
        }

        //TODO: logic
    }

    public static void branch_X(Options o, CommandLine cmd) {
        
        // cmd -X -t 0x00 -n c1 -d /path/to/export
        
        if (cmd.getOptions().length != 4 || cmd.getOptions().length != 5) {
            usage(o);
            return;
        }
        String repoType;
        String instanceName;
        String exportDir;

        if (cmd.hasOption("t")) {
            repoType = cmd.getOptionValue("t");
        } else {
            usage(o);
            return;
        }
        if (cmd.hasOption("n")) {
            instanceName = cmd.getOptionValue("n");
        } else {
            usage(o);
            return;
        }

        //TODO: logic
        
    }
    
    public static void branch_P(Options o, CommandLine cmd) {
        
        // cmd -P -t 0x00 -n c1 -M 5
        
        if (cmd.getOptions().length != 4 || cmd.getOptions().length != 5) {
            usage(o);
            return;
        }
        String repoType;
        String instanceName;
        String messageNo;

        if (cmd.hasOption("t")) {
            repoType = cmd.getOptionValue("t");
        } else {
            usage(o);
            return;
        }
        if (cmd.hasOption("n")) {
            instanceName = cmd.getOptionValue("n");
        } else {
            usage(o);
            return;
        }

        if (cmd.hasOption("M")) {
            messageNo = cmd.getOptionValue("M");
        } else {
            usage(o);
            return;
        }

        //TODO: logic
        
    }

    private static void usage(Options options) {

// Use the inbuilt formatter class
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("UcRepsoitoryAdmin", options);
    }
}

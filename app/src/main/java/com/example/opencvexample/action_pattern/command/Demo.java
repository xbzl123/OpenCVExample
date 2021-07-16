package com.example.opencvexample.action_pattern.command;

import java.util.ArrayList;
import java.util.List;

//对象类
class MouseCursor{
    private int x = 10;
    private int y = 10;
    public void move(){
        System.out.println("old cursor is "+x+","+y);
        x++;
        y++;
        System.out.println("new cursor is "+x+","+y);
    }

    public void reset(){
        System.out.println("reset");
        x = 10;
        y = 10;
    }
}
//行为接口
interface Command{
    void execute();
}
//操作实现类
class MoveCursor implements Command{

    private MouseCursor mouseCursor;

    MoveCursor(MouseCursor mouseCursor) {
        this.mouseCursor = mouseCursor;
    }

    @Override
    public void execute() {
        mouseCursor.move();
    }
}

class ResetCursor implements Command{

    private MouseCursor mouseCursor;

    public ResetCursor(MouseCursor mouseCursor) {
        this.mouseCursor = mouseCursor;
    }

    @Override
    public void execute() {
        mouseCursor.reset();
    }
}
//操作实现集合类
class MouseCommands{
    private List<Command> commandList = new ArrayList<>();

    public void addCommandList(Command command) {
        this.commandList.add(command);
    }

    public void actionAllCommand(){
        for (Command command:commandList){
            command.execute();
        }
        commandList.clear();
    }
}
class Demo {
    public static void main(String[] args) {
        MouseCommands mouseCommands = new MouseCommands();
        Command moveCursor = new MoveCursor(new MouseCursor());
        Command resetCursor = new ResetCursor(new MouseCursor());

        mouseCommands.addCommandList(moveCursor);
        mouseCommands.addCommandList(resetCursor);
        mouseCommands.actionAllCommand();
    }
}

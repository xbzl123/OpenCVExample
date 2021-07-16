package com.example.opencvexample.action_pattern.vistor_pattern;
//被访问者
class TreeNode{
    private String name;

    public TreeNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void accept(Vistor vistor){
        vistor.vist(this);
    }
}
//访问接口
interface Vistor{
    public void vist(TreeNode node);
}

//访问者
class ConsoleVistor implements Vistor{

    @Override
    public void vist(TreeNode node) {
        System.out.println("console vist to "+node.getName());
    }
}

class Demo {
    public static void main(String[] args) {
        TreeNode treeNode = new TreeNode("www.baidu.com");
        treeNode.accept(new ConsoleVistor());
    }
}

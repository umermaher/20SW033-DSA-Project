package com.example.dsaproject;

public class Node {
    private Model model;
    private Node next;
    private static int size;

    public Node(){}
    public Node(Model model) {
        this.model = model;
        size++;
    }

    public Node(Model model, Node next) {
        this.model = model;
        this.next = next;
        size++;
    }

    public void insert(Model model){
        Node p=null;
        for(p=this;p.next!=null;p=p.next){

        }
        p.next=new Node(model);
    }

    public int size(){
        return size;
    }
    public String toString(){
        String s="";
        for(Node p=this;p!=null;p=p.next){
            s+=" "+p.model;
        }
        return s;
    }

    public Model get(int i){
        Node p=this;
        int index=0;
        for(p=this;p!=null;p=p.next){
            if(index==i)
                return p.model;
            index++;
        }
        return null;
    }

    public Node addFirst(Model model){
        Node start=this;
        start=new Node(model,start);
        return start;
    }
}

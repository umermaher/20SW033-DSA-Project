package com.example.dsaproject;

public class LinkedList {
    Node start;
    private int size;

    private class Node{
        Model model;
        Node next;

        public Node(Model model) {
            this.model = model;
        }

        public Node(Model model, Node next) {
            this.model = model;
            this.next = next;
        }
    }

    public void insert(Model model){
        Node p=null;
        for(p=start;p.next!=null;p=p.next){

        }
        p.next=new Node(model);
    }

    public int getSize(){
        return size;
    }
    public String toString(){
        String s="";
        for(Node p = start; p!=null; p=p.next){
            s+=" "+p.model.getName();
        }
        return s;
    }

    public Model get(int i){
        int index=0;
        for(Node p=start;p!=null;p=p.next){
            if(index==i)
                return p.model;
            index++;
        }
        return null;
    }

    public void addFirst(Model model){
        start=new Node(model,start);
        size++;
    }

    public int search(String name){
        if(start==null)return -1;
//        if(model.getName().equals(name))return 0;
        int index=0;
        for(Node p = start; p!=null && p.model!=null; p=p.next){
            if(p.model.getName().equals(name))
                return index;
            index++;
        }
        return -1;
    }

    public void addAll(LinkedList updatedNews){
        for(Node p = updatedNews.start; p!=null; p=p.next){
            this.addFirst(new Model(p.model.getName(),p.model.getUri()));
        }
    }

    public void clear(){
        this.start=null;
        size=0;
    }
    public void setSize(int size){this.size=size;}
}






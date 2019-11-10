package com.newcoder.community.utils;

import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {
    private static final Logger logger= LoggerFactory.getLogger(SensitiveFilter.class);
    //前缀树，内部类

    //替换符
    private  static final String REPLACEMENT="***";
    //这个是个节点数据结构
    private class TrieNode{
        //关键词结束标识
        private boolean isKeywordEnd= false;

        //子节点(key是下级字符，value是下级节点)
        private Map<Character,TrieNode> subNodes=new HashMap<>();

        public boolean isKeywordEnd(){
            return isKeywordEnd;
        }
        public void setKeywordEnd(boolean keywordEnd){
            isKeywordEnd=keywordEnd;
        }
        //添加子节点的方法
        public void addSubNode(Character c, TrieNode node){

            subNodes.put(c,node);
        }
        //获取子节点
        public  TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }
    }

    private TrieNode rootNode=new TrieNode();//生成一个map，这个map不断的加子节点，这样的话就可以用map保存整棵树

    @PostConstruct
    public void init(){
        //从classPath下加载路径
    try (
        //将编译后生成的文件以字节流的方式读进来，字节流转换为字符流
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
        //将字节流转为缓冲流
        BufferedReader reader=new BufferedReader(new InputStreamReader(is));
    ) {
        String keyword;
        while ((keyword = reader.readLine()) != null) {
            //添加到前缀树
            this.addKeyword(keyword);
        }
    }
    catch(IOException e){
        logger.error("加载敏感词文件失败: "+e.getMessage());
    }

    }

//将一个敏感词添加到前缀树
    //生成树
    public  void addKeyword(String keyword){
        TrieNode tempNode=rootNode;
        //遍历单词的所有字符
        for(int i =0;i<keyword.length();i++){
            char c = keyword.charAt(i);
            TrieNode subNode= tempNode.getSubNode(c);//看看有没有这个字符的子节点
            if(subNode==null){//没有就new一个
                //初始化子节点
                subNode=new TrieNode();
                tempNode.addSubNode(c,subNode);
            }
            //指向子节点，进入下一轮循环
            tempNode=subNode;
            //设置结束的表示
            if(i==keyword.length()-1){
                tempNode.setKeywordEnd(true);
            }
        }
    }

    /**
     *过滤敏感词,供外部调用
     * @param text  待过滤的文本
     * @return  过滤后的文本
     */
    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return null;
        }
        TrieNode tempNode=rootNode;
        int begin=0;//前后指针
        int position=0;
        StringBuilder sb=new StringBuilder();
        while(position<text.length()){
            char c =text.charAt(position);
            if(tempNode=rootNode){
                sb.append(c);
            }
        }
    }
}

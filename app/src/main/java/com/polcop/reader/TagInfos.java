package com.polcop.reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by oleg on 09.09.14.
 */
public class TagInfos implements List<TagInfo> {

    private List<TagInfo> tagInfos;

    public TagInfos() {
        tagInfos = new ArrayList<TagInfo>();
    }

    public TagInfos(int initialCapacity) {
        tagInfos = new ArrayList<TagInfo>(initialCapacity);
    }

    public TagInfos(Collection<TagInfo> elements) {
        tagInfos = new ArrayList<TagInfo>(elements);
    }

    public TagInfos(List<TagInfo> elements) {
        tagInfos = elements;
    }

    public TagInfos(TagInfo... elements) {
        this(Arrays.asList(elements));
    }

    public String getLinkByName(String name){
        for (TagInfo tagInfo:tagInfos){
            if (tagInfo.getTagName()==name){
                return tagInfo.getTagURL();
            }
        }
        return null;
    }

    public String getNameByLink(String link){
        for (TagInfo tagInfo:tagInfos){
            if (tagInfo.getTagURL()==link){
                return tagInfo.getTagName();
            }
        }
        return null;    }

    public String getHtmlLinkByName(String name){
        for (TagInfo tagInfo:tagInfos){
            if (tagInfo.getTagName()==name){
                return tagInfo.getHtmlTag();
            }
        }
        return null;
    }

    public String getHtmlLinkByLink(String link){
        return getHtmlLinkByName(link);
    }

    //list implementation

    @Override
    public void add(int location, TagInfo object) {
        tagInfos.add(location,object);
    }

    @Override
    public boolean add(TagInfo object) {
        return tagInfos.add(object);
    }

    @Override
    public boolean addAll(int location, Collection<? extends TagInfo> collection) {
        return tagInfos.addAll(location, collection);
    }

    @Override
    public boolean addAll(Collection<? extends TagInfo> collection) {
        return tagInfos.addAll(collection);
    }

    @Override
    public void clear() {
        tagInfos.clear();
    }

    @Override
    public boolean contains(Object object) {
        return contains(object);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return containsAll(collection);
    }

    @Override
    public TagInfo get(int location) {
        return get(location);
    }

    @Override
    public int indexOf(Object object) {
        return tagInfos.indexOf(object);
    }

    @Override
    public boolean isEmpty() {
        return tagInfos.isEmpty();
    }

    @Override
    public Iterator<TagInfo> iterator() {
        return tagInfos.iterator();
    }

    @Override
    public int lastIndexOf(Object object) {
        return tagInfos.lastIndexOf(object);
    }

    @Override
    public ListIterator<TagInfo> listIterator() {
        return tagInfos.listIterator();
    }

    @Override
    public ListIterator<TagInfo> listIterator(int location) {
        return tagInfos.listIterator(location);
    }

    @Override
    public TagInfo remove(int location) {
        return tagInfos.remove(location);
    }

    @Override
    public boolean remove(Object object) {
        return tagInfos.remove(object);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return tagInfos.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return tagInfos.retainAll(collection);
    }

    @Override
    public TagInfo set(int location, TagInfo object) {
        return tagInfos.set(location,object);
    }

    @Override
    public int size() {
        return tagInfos.size();
    }

    @Override
    public List<TagInfo> subList(int start, int end) {
        return tagInfos.subList(start, end);
    }

    @Override
    public Object[] toArray() {
        return tagInfos.toArray();
    }

    @Override
    public <T> T[] toArray(T[] array) {
        return tagInfos.toArray(array);
    }
}

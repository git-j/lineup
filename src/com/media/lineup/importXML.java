package com.media.lineup;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

public class importXML{
  protected ModelAdapter model;
  public importXML(ModelAdapter model){
    this.model = model;
  }

  protected String text(Node node){
    if (node.getNodeType() != Node.ELEMENT_NODE)
      return "";
    StringBuffer buffer = new StringBuffer();
    NodeList childList = ((Element)node).getChildNodes();
    for (int i = 0; i < childList.getLength(); i++) {
      Node child = childList.item(i);
      if (child.getNodeType() != Node.TEXT_NODE)
        continue; // skip non-text nodes
      buffer.append(child.getNodeValue());
    }
    return buffer.toString();
  }
}

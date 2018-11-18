///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package controller.utils.converters;
//
//import controller.AprendizBean;
//import javax.el.ValueExpression;
//import javax.faces.component.UIComponent;
//import javax.faces.context.FacesContext;
//import javax.faces.convert.Converter;
//import javax.faces.convert.FacesConverter;
//import model.Programa;
//
///**
// *
// * @author danny
// */
//@FacesConverter(forClass = model.Programa.class, value = "programaConvertidor")
//public class ProgramaStringConverter implements Converter<Programa> {
//
//    @Override
//    public Programa getAsObject(FacesContext context, UIComponent component, String value) {
//
//        ValueExpression vex
//                = context.getApplication().getExpressionFactory()
//                        .createValueExpression(context.getELContext(),
//                                "#{aprendizBean}", AprendizBean.class);
//
//        AprendizBean apre = (AprendizBean) vex.getValue(context.getELContext());
//        return apre.getPrograma(Integer.valueOf(value));
//
//    }
//
//    @Override
//    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Programa prog) {
//
//        try {
//            return ((Programa) prog).getId().toString();
//        } catch (NullPointerException e) {
//            return "";
//        }
//
//    }
//
//}

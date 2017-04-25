/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fxexperience.tools.caspianstyler;

/**
 *
 * @author Eric Canull
 */
public class CaspianStringUtil {

    public static String padWithSpaces(String s, int length) {

        StringBuilder sb = new StringBuilder();
        while (sb.length() < length) {
            sb.append(" ");
        }
        return sb.append(s).toString();
    }
}

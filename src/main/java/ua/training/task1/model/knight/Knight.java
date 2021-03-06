/*
 * @(#) Knight.java
 *
 * MIT License
 *
 * Copyright (c) 2019 Dashchyk Andrey
 *
 * Permission is hereby granted, free of charge,to any
 * person obtaining a copy of this software and
 * associated documentation files (the "Software"), to
 * deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to
 * whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission
 * notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY
 * OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES
 * OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM,  OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */


package ua.training.task1.model.knight;

import ua.training.task1.InitBodyParts;
import ua.training.task1.model.ammunition.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Did not implement sort!!
 * Did not implement choose!!
 *
 * @author      Dashchyk Andrey
 */
public class Knight {
    private static final EnumSet<InitBodyParts> bodyPartsNames;
    private Body body = new Body(bodyPartsNames);

    static {
        bodyPartsNames = EnumSet.allOf(InitBodyParts.class);
    }

    /**
     * @param ammunitionMap key is bodyPart to wear on and value is ammunition
     */
    public Knight(HashMap<String, WarObject> ammunitionMap) {
        for(String key : ammunitionMap.keySet()) {
            body.wearAmmunitionOnExistBodyPart(key, ammunitionMap.get(key));
        }
    }

    /**
     * @return from min to max exist ammunition
     */
    public List<WarObject> sortAmmunitionByPrice() {
        return  body.getBodyPartsNames().stream()
                .map(key -> body.getWarObjectFromBodyPart(key))
                .filter(Objects::nonNull)
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * @param minBorder such as >= minBorder
     * @param maxBorder such as <= minBorder
     * @return
     */
    public List<WarObject> findAmmunitionInPriceRange(double minBorder, double maxBorder) {
        return body.getBodyPartsNames().stream()
                .map(key -> body.getWarObjectFromBodyPart(key))
                .filter(Objects::nonNull)
                .filter(warObject -> minBorder <= warObject.price())
                .filter(warObject -> warObject.price() <= maxBorder)
                .collect(Collectors.toList());
    }

    /**
     * Multiply sliceDamage and coefficient for all weapons
     */
    public void sharpenAllWeapons(double coefficient) {
        body.getBodyPartsNames().stream()
                .map(key -> body.getWarObjectFromBodyPart(key))
                .filter(warObject -> warObject instanceof Sizable)
                .map(warObject -> (Sizable) warObject)
                .forEach(sizable -> sizable.sharpen(coefficient));
    }

    public static EnumSet<InitBodyParts> getBodyPartsNames() {
        return bodyPartsNames;
    }

    /**
     * Calculate sum of all three types of damage
     * 
     * @return sum
     */
    public double countResistPerCycle() {
        return countDamageAmountPerAttack(Armor.class);
    }

    /**
     * Calculate sum of all three types of resist
     *
     * @return sum
     */
    public double countDamagePerCycle() {
        return countDamageAmountPerAttack(Weapon.class);
    }

    private double countDamageAmountPerAttack(Class<?> subClass) {
        return body.getBodyPartsNames().stream()
                .map(key -> body.getWarObjectFromBodyPart(key))
                .filter(Objects::nonNull)
                .filter(subClass::isInstance)
                .map(warObject ->
                        warObject.impactDamage() +
                        warObject.sliceDamage() +
                        warObject.pierceDamage())
                .reduce(0.0, Double::sum);
    }
}

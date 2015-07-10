/*
 * SonarQube Java Properties Plugin
 * Copyright (C) 2015 David RACODON
 * david.racodon@gmail.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.jproperties.checks;

import java.io.File;

import org.junit.Test;
import org.sonar.jproperties.JavaPropertiesAstScanner;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.CheckMessagesVerifier;

public class KeyNamingConventionCheckTest {

  private KeyNamingConventionCheck check = new KeyNamingConventionCheck();

  @Test
  public void should_find_some_keys_that_do_not_follow_the_default_naming_convention_and_raise_issues() {
    SourceFile file = JavaPropertiesAstScanner.scanSingleFile(new File("src/test/resources/checks/keyNamingConvention.properties"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages()).next()
      .atLine(2).withMessage("Rename key \"myProperty\" to match the regular expression: ^[a-z]+[a-z0-9_\\-\\.]*$").next()
      .atLine(3).withMessage("Rename key \"my\\:Property\" to match the regular expression: ^[a-z]+[a-z0-9_\\-\\.]*$").noMore();
  }

  @Test
  public void should_find_some_keys_that_do_not_follow_a_custom_naming_convention_and_raise_issues() {
    check.setFormat("a.*");
    SourceFile file = JavaPropertiesAstScanner.scanSingleFile(new File("src/test/resources/checks/keyNamingConvention.properties"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages()).next()
      .atLine(1).withMessage("Rename key \"my.property\" to match the regular expression: a.*").next()
      .atLine(2).withMessage("Rename key \"myProperty\" to match the regular expression: a.*").next()
      .atLine(3).withMessage("Rename key \"my\\:Property\" to match the regular expression: a.*").noMore();
  }

}
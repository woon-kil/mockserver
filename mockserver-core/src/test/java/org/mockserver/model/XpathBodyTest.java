package org.mockserver.model;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

import com.google.common.collect.ImmutableMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.mockserver.model.XPathBody.xpath;

/**
 * @author jamesdbloom
 */
public class XpathBodyTest {

    @Test
    public void shouldReturnValuesSetInConstructor() {
        // when
        XPathBody xPathBody = new XPathBody("some_body");

        // then
        assertThat(xPathBody.getValue(), is("some_body"));
        assertThat(xPathBody.getNamespacePrefixes(), nullValue());
        assertThat(xPathBody.getType(), is(Body.Type.XPATH));
        assertThat(xPathBody.getContentType(), nullValue());
        assertThat(xPathBody.getCharset(StandardCharsets.UTF_8), is(StandardCharsets.UTF_8));
    }


    @Test
    public void shouldReturnValuesSetInConstructorWithNamespacePrefixes() {
        // when
        XPathBody xPathBody = new XPathBody("some_body", ImmutableMap.of("foo", "http://foo"));

        // then
        assertThat(xPathBody.getValue(), is("some_body"));
        assertThat(xPathBody.getNamespacePrefixes(), is(ImmutableMap.of("foo", "http://foo")));
        assertThat(xPathBody.getType(), is(Body.Type.XPATH));
        assertThat(xPathBody.getContentType(), nullValue());
        assertThat(xPathBody.getCharset(StandardCharsets.UTF_8), is(StandardCharsets.UTF_8));
    }

    @Test
    public void shouldReturnValuesFromStaticBuilder() {
        // when
        XPathBody xPathBody = xpath("some_body");

        // then
        assertThat(xPathBody.getValue(), is("some_body"));
        assertThat(xPathBody.getNamespacePrefixes(), nullValue());
        assertThat(xPathBody.getType(), is(Body.Type.XPATH));
        assertThat(xPathBody.getContentType(), nullValue());
        assertThat(xPathBody.getCharset(StandardCharsets.UTF_8), is(StandardCharsets.UTF_8));
    }


    @Test
    public void shouldReturnValuesFromStaticBuilderWithNamespacePrefixes() {
        // when
        XPathBody xPathBody = xpath("some_body", ImmutableMap.of("foo", "http://foo"));

        // then
        assertThat(xPathBody.getValue(), is("some_body"));
        assertThat(xPathBody.getNamespacePrefixes(), is(ImmutableMap.of("foo", "http://foo")));
        assertThat(xPathBody.getType(), is(Body.Type.XPATH));
        assertThat(xPathBody.getContentType(), nullValue());
        assertThat(xPathBody.getCharset(StandardCharsets.UTF_8), is(StandardCharsets.UTF_8));
    }

}

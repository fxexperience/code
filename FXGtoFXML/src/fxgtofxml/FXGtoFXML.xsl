<xsl:transform version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fxg="http://ns.adobe.com/fxg/2008" xmlns:ai="http://ns.adobe.com/ai/2008">

    <xsl:template match="fxg:Graphic">
        <xsl:processing-instruction name="import">javafx.scene.*</xsl:processing-instruction>
        <xsl:processing-instruction name="import">javafx.scene.shape.*</xsl:processing-instruction>
        <xsl:processing-instruction name="import">javafx.scene.paint.*</xsl:processing-instruction>
        <xsl:processing-instruction name="import">javafx.scene.image.*</xsl:processing-instruction>
        <Group id="Document" xmlns:fx="http://javafx.com/fxml">
            <children><xsl:apply-templates/></children>
        </Group>
    </xsl:template>

    <xsl:template match="fxg:Group">
        <Group>
            <xsl:if test="@x != ''"><xsl:attribute name="translateX"><xsl:value-of select="attribute::x"/></xsl:attribute></xsl:if>
            <xsl:if test="@y != ''"><xsl:attribute name="translateY"><xsl:value-of select="attribute::y"/></xsl:attribute></xsl:if>
            <xsl:if test="@visible != ''"><xsl:attribute name="visible"><xsl:value-of select="attribute::visible"/></xsl:attribute></xsl:if>
            <xsl:if test="@alpha != ''"><xsl:attribute name="opacity"><xsl:value-of select="attribute::alpha"/></xsl:attribute></xsl:if>
            <xsl:if test="@scaleX != ''"><xsl:attribute name="scaleX"><xsl:value-of select="attribute::scaleX"/></xsl:attribute></xsl:if>
            <xsl:if test="@scaleY != ''"><xsl:attribute name="scaleY"><xsl:value-of select="attribute::scaleY"/></xsl:attribute></xsl:if>
            <children><xsl:apply-templates/></children>
        </Group>
    </xsl:template>

    <xsl:template match="fxg:BitmapGraphic">
        <ImageView >
            <xsl:if test="@x != ''"><xsl:attribute name="translateX"><xsl:value-of select="attribute::x"/></xsl:attribute></xsl:if>
            <xsl:if test="@y != ''"><xsl:attribute name="translateY"><xsl:value-of select="attribute::y"/></xsl:attribute></xsl:if>
            <xsl:if test="@visible != ''"><xsl:attribute name="visible"><xsl:value-of select="attribute::visible"/></xsl:attribute></xsl:if>
            <xsl:if test="@alpha != ''"><xsl:attribute name="opacity"><xsl:value-of select="attribute::alpha"/></xsl:attribute></xsl:if>
            <xsl:choose>
                <xsl:when test="@scaleX != '' and @width !=''"><xsl:attribute name="fitWidth"><xsl:value-of select="attribute::width * attribute::scaleX"/></xsl:attribute></xsl:when>
                <xsl:when test="@width != ''"><xsl:attribute name="fitWidth"><xsl:value-of select="attribute::width"/></xsl:attribute></xsl:when>
                <xsl:when test="@scaleX != ''"><xsl:attribute name="scaleX"><xsl:value-of select="attribute::scaleX"/></xsl:attribute></xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="@scaleY != '' and @height !=''"><xsl:attribute name="fitHeight"><xsl:value-of select="attribute::height * attribute::scaleY"/></xsl:attribute></xsl:when>
                <xsl:when test="@height != ''"><xsl:attribute name="fitHeight"><xsl:value-of select="attribute::height"/></xsl:attribute></xsl:when>
                <xsl:when test="@scaleY != ''"><xsl:attribute name="scaleY"><xsl:value-of select="attribute::scaleY"/></xsl:attribute></xsl:when>
            </xsl:choose>
            <image><Image url="@{substring-before(substring(attribute::source,9),&quot;&apos;&quot;)}"/></image>
        </ImageView>
    </xsl:template>

    <xsl:template match="fxg:Path">
        <SVGPath content="{attribute::data}">
            <xsl:if test="@visible != ''"><xsl:attribute name="visible"><xsl:value-of select="attribute::visible"/></xsl:attribute></xsl:if>
            <xsl:if test="@alpha != ''"><xsl:attribute name="opacity"><xsl:value-of select="attribute::alpha"/></xsl:attribute></xsl:if>
            <xsl:if test="@blendMode != ''"><xsl:attribute name="blendMode"><xsl:value-of select="attribute::blendMode"/></xsl:attribute></xsl:if>
            <xsl:apply-templates/>
        </SVGPath>
    </xsl:template>

    <xsl:template match="fxg:Rect">
        <Rectangle>
            <xsl:if test="@x != ''"><xsl:attribute name="x"><xsl:value-of select="attribute::x"/></xsl:attribute></xsl:if>
            <xsl:if test="@y != ''"><xsl:attribute name="y"><xsl:value-of select="attribute::y"/></xsl:attribute></xsl:if>
            <xsl:if test="@width != ''"><xsl:attribute name="width"><xsl:value-of select="attribute::width"/></xsl:attribute></xsl:if>
            <xsl:if test="@height != ''"><xsl:attribute name="height"><xsl:value-of select="attribute::height"/></xsl:attribute></xsl:if>
            <xsl:apply-templates/>
        </Rectangle>
    </xsl:template>

    <xsl:template match="fxg:Ellipse">
        <Ellipse>
            <xsl:if test="@x != ''"><xsl:attribute name="centerX"><xsl:value-of select="attribute::x + (attribute::width * 0.5)"/></xsl:attribute></xsl:if>
            <xsl:if test="@y != ''"><xsl:attribute name="centerY"><xsl:value-of select="attribute::y + (attribute::height * 0.5)"/></xsl:attribute></xsl:if>
            <xsl:if test="@width != ''"><xsl:attribute name="radiusX"><xsl:value-of select="attribute::width * 0.5"/></xsl:attribute></xsl:if>
            <xsl:if test="@height != ''"><xsl:attribute name="radiusY"><xsl:value-of select="attribute::height * 0.5"/></xsl:attribute></xsl:if>
            <xsl:apply-templates/>
        </Ellipse>
    </xsl:template>

    <xsl:template match="fxg:fill/fxg:SolidColor">
        <xsl:if test="@color != ''"><xsl:attribute name="fill"><xsl:value-of select="attribute::color"/></xsl:attribute></xsl:if>
    </xsl:template>

    <xsl:template match="fxg:fill/fxg:RadialGradient">
        <fill>
            <RadialGradient centerX="{attribute::x}" centerY="{attribute::y}" radius="{attribute::scaleX}" proportional="false">
                <stops>
                    <xsl:apply-templates/>
                </stops>
            </RadialGradient>
        </fill>
    </xsl:template>

    <xsl:template match="fxg:fill/fxg:LinearGradient">
        <fill>
            <LinearGradient startX="{attribute::x}" startY="{attribute::y}" endX="{attribute::scaleX}" proportional="false">
                <xsl:choose>
                    <xsl:when test="@scaleY != ''"><xsl:attribute name="endY"><xsl:value-of select="attribute::scaleY"/></xsl:attribute></xsl:when>
                    <xsl:otherwise><xsl:attribute name="endY"><xsl:value-of select="attribute::scaleX"/></xsl:attribute></xsl:otherwise>
                </xsl:choose>
                <stops>
                    <xsl:apply-templates/>
                </stops>
            </LinearGradient>
        </fill>
    </xsl:template>

    <xsl:template match="fxg:GradientEntry">
        <Stop offset="{attribute::ratio}" color="{attribute::color}"/>
    </xsl:template>

    <xsl:template match="text()|@*"></xsl:template>
</xsl:transform>
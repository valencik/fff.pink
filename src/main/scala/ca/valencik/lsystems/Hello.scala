package ca.valencik.lsystems
import scala.annotation.tailrec

object Writer {
  import java.nio.file.{Paths, Files, StandardOpenOption}
  import java.nio.charset.{StandardCharsets}
  import scala.collection.JavaConverters._

  def write(filePath: String, contents: String) = {
    Files.write(
      Paths.get(filePath),
      contents.getBytes(StandardCharsets.UTF_8),
      StandardOpenOption.TRUNCATE_EXISTING
    )
  }
}

case class Page(title: String, path: String) {
  val template = s"""
    |<!DOCTYPE html>
    |<html>
    |<head>
    |<style>
    |body {background-color: #333333;}
    |#outer {
    |  width: 100%;
    |  text-align: center;
    |}
    |
    |#inner {
    |  display: inline-block;
    |}
    |</style>
    |</head>
    |<body>
    |
    |<div id="outer">
    |<div id="inner">
    |<svg class="path" width="100%" height="400" viewBox="0 0 400 200">
    |  <defs>
    |    <linearGradient id="grad1" x1="0%" y1="0%" x2="0%" y2="100%">
    |      <stop offset="0%" style="stop-color:#CD66E8;stop-opacity:1" />
    |      <stop offset="100%" style="stop-color:#7074FF;stop-opacity:1" />
    |    </linearGradient>
    |  </defs>
    |  <path d="M2 2 ${path}"  stroke="url(#grad1)"
    |  stroke-width="2" fill="none" stroke-linecap="round" />
    |</svg>
    |</div> 
    |</div> 
    | 
    |</body>
    |</html>
    |""".stripMargin
}

case class Rule(find: String, replace: String) {
  val cfind: Char                       = find.toList.head
  val creplace                          = replace.toCharArray()
  def apply(input: String): Array[Char] = apply(input.toArray)
  def apply(cs: Array[Char]): Array[Char] = cs.flatMap {
    case c => if (c == cfind) creplace else Array(c)
  }
}

object Hello extends App {
  def polar2xy(radius: Double, angle: Double): Tuple2[Double, Double] =
    (radius * Math.cos(angle), radius * Math.sin(angle))

  def xy2path(xy: Tuple2[Double, Double]): String = {
    s" l${xy._1.toInt}.${(xy._1 * 100).toInt % 100} ${xy._2.toInt}.${(xy._2 * 100).toInt % 100}"
  }

  def process(input: Array[Char], deg: Int): String = {
    val stepAngle = Math.toRadians(deg)
    @tailrec
    def inner(acc: String, cs: Array[Char], angle: Double): String = cs match {
      case Array(h, t @ _*) if (h == 'F') =>
        inner(acc + xy2path(polar2xy(length, angle)), t.toArray, angle)
      case Array(h, t @ _*) if (h == '+') =>
        inner(acc, t.toArray, angle + stepAngle)
      case Array(h, t @ _*) if (h == '-') =>
        inner(acc, t.toArray, angle - stepAngle)
      case Array(h, t @ _*) if (h == 'X') => inner(acc, t.toArray, angle)
      case c                              => acc
    }
    inner("", input, 0.0)
  }

  val length = 6.0
  val koch   = Rule("F", "F+F--F+F")
  val quirk  = Rule("X", "XF+F-F+FX-F-FX+F-F+X")

  val fstring = quirk(quirk(quirk("XF+F-F+XF--XF+F-F+XF")))
  println(s"fstring has size: ${fstring.size}")

  val page = Page("This is a test", process(fstring, 90))
  Writer.write("./output.html", page.template)
  println("fin")
}

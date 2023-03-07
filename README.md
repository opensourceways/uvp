# Welcome to the UVP Project

UVP is a **U**nified **V**ulnerability **P**latform about vulnerabilities that affect open source software.
It's written in Java 17 and built with Gradle.

# Table of Contents

- [Introduction](#uvp---unified-vulnerability-platform)
    - [Architecture](docs/architecture.md)
    - [Data Model](docs/data-model.md)
    - [Integrated Vulnerability Databases](#integrated-vulnerability-databases)
- [Deployment](docs/deployment.md)
- [Using UVP](#using-uvp)
    - [Viewing the Website](#viewing-the-website)
    - [Using the API](#using-the-api)

# UVP - Unified Vulnerability Platform

UVP is a unified vulnerability platform that provides open source software vulnerability retrieval
and diagnosis service for projects.

Through periodical aggregation of multiple vulnerability databases, UVP provides timely vulnerability information.
Additionally, with heuristic policies, UVP tries to avoid false positives and false negatives.

# Integrated Vulnerability Databases

## Open Source

* [OSV](https://osv.dev)
* [OSS Index](https://ossindex.sonatype.org)
* [NVD](https://nvd.nist.gov)

## Commercial

* [Vtopia](http://www.vulab.com.cn/vtopia.html)
  (**note:** only vulnerabilities affecting [openEuler](https://www.openeuler.org) packages)
* [7-cai](https://www.7-cai.com)
  (**note:** only vulnerabilities affecting [OpenHarmony](https://www.openharmony.cn) packages)

# Using UVP

## Viewing the Website

TODO

## Using the API

TODO
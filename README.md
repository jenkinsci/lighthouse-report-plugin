# lighthouse-report-plugin

A simple app that displays a lighthouse report inside jenkins.

## Usage

```groovy
// Generate your lighthouse report. 
sh("npx lighthouse-ci https://www.example.com --jsonReport --report=.")
lighthouseReport(readFile('./report.json'))
```


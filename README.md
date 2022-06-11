<div id="top"></div>

<!-- PROJECT LOGO -->
<br />
<div align="center">

<a href="https://imgbb.com/"><img src="https://i.ibb.co/XWmt9KB/islamqa.png" alt="islamqa" border="0" width="240" height="220"></a>

<h3 align="center">IslamQA-for-Android</h3>

  <p align="center">
     Native android application built on top of https://islamqa.org/.
    <br />
    <br />
    <a href="https://github.com/kabirnayeem99/islam_qa_org_android/issues">View Demo</a>
    ·
    <a href="https://github.com/kabirnayeem99/islam_qa_org_android/issues">Report Bug</a>
    ·
    <a href="https://github.com/kabirnayeem99/islam_qa_org_android/issues">Request Feature</a>
    .
    <a href="https://github.com/users/kabirnayeem99/projects/1">Project Management</a>
  </p>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->

## About The Project

Native Android Application built with latest Android OS features. Just a frontend for IslamQA.org.

IslamQA.org is a repository of Islamic answers. They have collected over 93,950 Islamic Q&A from the
official websites of various scholars and Islamic educational institutions around the world. You can
find answers to your questions from different school of thoughts (madaahib) from the Sunni
scholarship. So far, they have collected around 87,000 answers from Hanafi Fiqh, around 5,700
answers from Shafi’i Fiqh, around 270 answers from Maliki Fiqh, and around 280 answers from the
Hanbali fiqh.

This app just scrapes this awesome website in the user-end, and shows the answers based on the
preferred 'Madhab' of the user. Once loaded, it caches the data, to reduce load on the original
website. This app incorporates latest Material You themeing, Clean Architecture recommended by
Google, Kotlin Coroutines for multithreading and Work Manager for background syncing to give an
immersive experience to the user.

<p align="right">(<a href="#top">back to top</a>)</p>

### Built With

Major frameworks this application is built around.

* Android SDK
* Kotlin Coroutines, for multithreading.
* [skrape.it](https://github.com/skrapeit/skrape.it), for web scraping.

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- GETTING STARTED -->

## Getting Started

This app needs no extra credential as it has no internal API or Firebase connection.

### Prerequisites

* Android Studio Chipmunk ([Download link](https://developer.android.com/studio/releases))
* Java-OpenJDK 11+
    * For macOS
       ```shell
       brew install openjdk@11
       ```
    * For Windows
       ```shell
        choco install openjdk11
       ```
    * For Linux
       ```shell
        pacman -S jdk11-openjdk
        ```
* Android SDK Version 32

### Installation

1. Clone the repo
   ```shell
   git clone https://github.com/kabirnayeem99/islam_qa_org_android.git
   ```
2. Build the project
    ```shell
    ./gradlew build
    ```

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- USAGE EXAMPLES -->

## Demo

<a href="https://ibb.co/s1n33f5"><img src="https://i.ibb.co/pKcXX7W/demo-home-screen.png" alt="demo-home-screen" border="0" height="500"></a>
<a href="https://ibb.co/PZWxGz4"><img src="https://i.ibb.co/4ZSWRgM/demo-details-page.png" alt="demo-details-page" border="0"  height="500"></a>

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- ROADMAP -->

## Roadmap

- [x] Load Random questions in a slider
- [x] Load Fiqh-based questions in the home page
- [x] Load Answers with the relevant question list.
- [ ] Offline first designs with background syncing.

See the [open issues](https://github.com/kabirnayeem99/islam_qa_org_android/issues) for a full list
of proposed features (and known issues).

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- CONTRIBUTING -->

## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and
create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull
request. You can also simply open an issue with the tag "enhancement". Don't forget to give the
project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- LICENSE -->

## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- CONTACT -->

## Contact

Naimul Kabir - [@kabirnayeem99](https://www.linkedin.com/in/kabirnayeem99/) -
kabirnayeem.99@gmail.com

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- ACKNOWLEDGMENTS -->

## Acknowledgments

* [skrape.it](https://github.com/skrapeit/skrape.it)
* [Shimmer Effect for Android](https://facebook.github.io/shimmer-android/)
* [Timber](https://github.com/JakeWharton/timber)

<p align="right">(<a href="#top">back to top</a>)</p>


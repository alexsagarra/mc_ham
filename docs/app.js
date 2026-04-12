(function () {
  var owner = "alexsagarra";
  var repo = "mc_ham";

  var latestRelease = "https://github.com/" + owner + "/" + repo + "/releases/latest";
  var directJar = "https://github.com/" + owner + "/" + repo + "/releases/latest/download/mc_hammer-0.1.0.jar";

  var latestBtn = document.getElementById("downloadLatest");
  var releasesBtn = document.getElementById("releasesPage");
  var repoLabel = document.getElementById("repoLabel");

  if (latestBtn) {
    latestBtn.href = directJar;
  }

  if (releasesBtn) {
    releasesBtn.href = latestRelease;
  }

  if (repoLabel) {
    repoLabel.textContent = "Repository: " + owner + "/" + repo;
  }
})();

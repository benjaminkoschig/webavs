function roundToFiveCentimes(v) {
  return parseFloat((Math.round(v*20)/20).toFixed(2));
}
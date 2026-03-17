import { useEffect, useRef } from 'react';
import type { Point, Route } from '../api/types';

const PADDING = 32;

function drawRoute(
  ctx: CanvasRenderingContext2D,
  w: number,
  h: number,
  points: Point[],
  route: Route | null
): void {
  ctx.clearRect(0, 0, w, h);

  if (points.length === 0) {
    ctx.fillStyle = '#9e9e9e';
    ctx.font = '16px system-ui, sans-serif';
    ctx.textAlign = 'center';
    ctx.textBaseline = 'middle';
    ctx.fillText('Load a problem to get started', w / 2, h / 2);
    return;
  }

  const minX = Math.min(...points.map((p) => p.x));
  const maxX = Math.max(...points.map((p) => p.x));
  const minY = Math.min(...points.map((p) => p.y));
  const maxY = Math.max(...points.map((p) => p.y));
  const spanX = maxX - minX;
  const spanY = maxY - minY;

  const scale = Math.min(
    (w - 2 * PADDING) / Math.max(spanX, 1),
    (h - 2 * PADDING) / Math.max(spanY, 1)
  );

  const cx = (p: Point) => PADDING + (p.x - minX) * scale;
  const cy = (p: Point) => h - PADDING - (p.y - minY) * scale;

  if (route && route.length > 0) {
    const ordered = route.map((idx) => points[idx - 1]);
    ctx.beginPath();
    ctx.moveTo(cx(ordered[0]), cy(ordered[0]));
    for (let i = 1; i < ordered.length; i++) {
      ctx.lineTo(cx(ordered[i]), cy(ordered[i]));
    }
    ctx.closePath();
    ctx.strokeStyle = '#1976d2';
    ctx.lineWidth = 2;
    ctx.stroke();
  }

  points.forEach((p, i) => {
    const isFirst = i === 0;
    ctx.beginPath();
    ctx.arc(cx(p), cy(p), isFirst ? 6 : 4, 0, Math.PI * 2);
    ctx.fillStyle = isFirst ? '#c62828' : '#ffb300';
    ctx.fill();
    ctx.strokeStyle = '#424242';
    ctx.lineWidth = 1;
    ctx.stroke();
  });
}

export function useCanvasRenderer(
  canvasRef: React.RefObject<HTMLCanvasElement | null>,
  points: Point[],
  route: Route | null
): void {
  const dimensionsRef = useRef<{ w: number; h: number }>({ w: 0, h: 0 });

  useEffect(() => {
    const canvas = canvasRef.current;
    if (!canvas) return;

    const container = canvas.parentElement;
    if (!container) return;

    const observer = new ResizeObserver((entries) => {
      const entry = entries[0];
      if (!entry) return;
      const { width, height } = entry.contentRect;
      const dpr = window.devicePixelRatio || 1;
      canvas.width = width * dpr;
      canvas.height = height * dpr;
      canvas.style.width = `${width}px`;
      canvas.style.height = `${height}px`;
      const ctx = canvas.getContext('2d');
      if (!ctx) return;
      ctx.scale(dpr, dpr);
      dimensionsRef.current = { w: width, h: height };
      drawRoute(ctx, width, height, points, route);
    });

    observer.observe(container);
    return () => observer.disconnect();
  }, [canvasRef, points, route]);

  useEffect(() => {
    const canvas = canvasRef.current;
    if (!canvas) return;
    const ctx = canvas.getContext('2d');
    if (!ctx) return;
    const { w, h } = dimensionsRef.current;
    if (w === 0 || h === 0) return;
    // Reset transform to account for DPR scaling already set by observer
    const dpr = window.devicePixelRatio || 1;
    ctx.setTransform(dpr, 0, 0, dpr, 0, 0);
    drawRoute(ctx, w, h, points, route);
  }, [canvasRef, points, route]);
}
